import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Free Render deployment: static UI, URL API, redirect, and stats in one process. */
public final class UrlShortenerServer {
  private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final Pattern URL = Pattern.compile("\\\"url\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"");
  private static final Pattern CODE = Pattern.compile("^[A-Za-z0-9]{7}$");
  private static final Map<String, Link> LINKS = new ConcurrentHashMap<>();
  private static Path publicDir;
  private record Link(String url, long created, long clicks) { Link clicked() { return new Link(url, created, clicks + 1); } }

  public static void main(String[] args) throws IOException {
    publicDir = Path.of(System.getenv().getOrDefault("STATIC_DIR", "public"));
    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", UrlShortenerServer::handle);
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
  }

  private static void handle(HttpExchange x) throws IOException {
    try {
      String path = x.getRequestURI().getPath();
      if ("OPTIONS".equals(x.getRequestMethod())) empty(x, 204);
      else if ("POST".equals(x.getRequestMethod()) && "/api/urls".equals(path)) shorten(x);
      else if ("GET".equals(x.getRequestMethod()) && path.startsWith("/api/analytics/")) stats(x, path.substring(15));
      else if ("GET".equals(x.getRequestMethod()) && CODE.matcher(path.substring(1)).matches()) redirect(x, path.substring(1));
      else if ("GET".equals(x.getRequestMethod())) asset(x, path);
      else json(x, 404, "{\"error\":\"Tapılmadı\"}");
    } catch (Exception error) { error.printStackTrace(); json(x, 500, "{\"error\":\"Server xətası\"}"); }
    finally { x.close(); }
  }

  private static void shorten(HttpExchange x) throws IOException {
    String body = new String(x.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    Matcher match = URL.matcher(body);
    if (!match.find()) { json(x, 400, "{\"url\":\"Etibarlı URL daxil edin.\"}"); return; }
    String original = match.group(1).replace("\\\"", "\"").replace("\\\\", "\\");
    try { URI u = URI.create(original); if (u.getHost() == null || !("http".equals(u.getScheme()) || "https".equals(u.getScheme()))) throw new IllegalArgumentException(); }
    catch (IllegalArgumentException error) { json(x, 400, "{\"url\":\"Etibarlı http və ya https URL daxil edin.\"}"); return; }
    String code; do { code = code(); } while (LINKS.containsKey(code));
    LINKS.put(code, new Link(original, 1, 0));
    json(x, 201, "{\"originalUrl\":\"" + escape(original) + "\",\"shortCode\":\"" + code + "\",\"shortUrl\":\"" + base(x) + "/" + code + "\"}");
  }

  private static void stats(HttpExchange x, String code) throws IOException {
    Link link = LINKS.get(code);
    if (link == null) { json(x, 404, "{\"error\":\"Qısa link tapılmadı.\"}"); return; }
    json(x, 200, "{\"shortCode\":\"" + code + "\",\"totalClicks\":" + link.clicks + ",\"createdEvents\":" + link.created + "}");
  }
  private static void redirect(HttpExchange x, String code) throws IOException {
    Link link = LINKS.computeIfPresent(code, (key, value) -> value.clicked());
    if (link == null) { json(x, 404, "{\"error\":\"Qısa link tapılmadı.\"}"); return; }
    x.getResponseHeaders().set("Location", link.url); empty(x, 302);
  }
  private static void asset(HttpExchange x, String path) throws IOException {
    Path file = publicDir.resolve("/".equals(path) ? "index.html" : path.substring(1)).normalize();
    if (!file.startsWith(publicDir) || !Files.isRegularFile(file)) file = publicDir.resolve("index.html");
    byte[] data = Files.readAllBytes(file);
    x.getResponseHeaders().set("Content-Type", file.toString().endsWith(".css") ? "text/css; charset=utf-8" : file.toString().endsWith(".js") ? "application/javascript; charset=utf-8" : "text/html; charset=utf-8");
    x.sendResponseHeaders(200, data.length); x.getResponseBody().write(data);
  }
  private static void json(HttpExchange x, int status, String body) throws IOException { byte[] data = body.getBytes(StandardCharsets.UTF_8); x.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8"); x.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); x.sendResponseHeaders(status, data.length); x.getResponseBody().write(data); }
  private static void empty(HttpExchange x, int status) throws IOException { x.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); x.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); x.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type"); x.sendResponseHeaders(status, -1); }
  private static String base(HttpExchange x) { String host = x.getRequestHeaders().getFirst("X-Forwarded-Host"); if (host == null) host = x.getRequestHeaders().getFirst("Host"); String scheme = x.getRequestHeaders().getFirst("X-Forwarded-Proto"); return (scheme == null ? "http" : scheme) + "://" + host; }
  private static String code() { StringBuilder b = new StringBuilder(7); for (int i = 0; i < 7; i++) b.append(CHARS.charAt(RANDOM.nextInt(CHARS.length()))); return b.toString(); }
  private static String escape(String value) { return value.replace("\\", "\\\\").replace("\"", "\\\""); }
}

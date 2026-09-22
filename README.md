# URL Qısaldıcı

Uzun URL-ləri qısaldan, qısa linkləri yönləndirən və sadə klik statistikasını göstərən veb tətbiq.

## Canlı demo

[https://link-shortner-ku3n.onrender.com/](https://link-shortner-ku3n.onrender.com/)

## İmkanlar

- Tam URL-dən yeddi simvollu qısa kod yaradır.
- Qısa link açıldıqda orijinal ünvanına yönləndirir.
- Yaradılma hadisəsi və klik sayını göstərir.
- Linki bir kliklə clipboard-a köçürür.
- Mobil ekranlara uyğun sadə Vanilla HTML/CSS/JavaScript interfeysi təqdim edir.

## API

| Metod | Endpoint | Təyinat |
|---|---|---|
| `POST` | `/api/urls` | Yeni qısa link yaradır |
| `GET` | `/api/analytics/{shortCode}` | Link statistikasını qaytarır |
| `GET` | `/{shortCode}` | Orijinal URL-ə yönləndirir |

### Link yaratmaq

```bash
curl -X POST https://link-shortner-ku3n.onrender.com/api/urls \
  -H "Content-Type: application/json" \
  -d '{"url":"https://example.com"}'
```

Nümunə cavab:

```json
{
  "originalUrl": "https://example.com",
  "shortCode": "aB3xYz7",
  "shortUrl": "https://link-shortner-ku3n.onrender.com/aB3xYz7"
}
```

## Pulsuz Render deploy-u

Canlı versiya Render-in Free Web Service planında tək Docker konteyneri kimi çalışır. `free-render/UrlShortenerServer.java` frontend-i, API-ni, yönləndirməni və statistikanı eyni prosesdə təqdim edir. Bu yanaşma ayrıca MySQL, RabbitMQ və Eureka servislərinə ehtiyacı aradan qaldırır.

Render deploy parametrləri:

- Runtime: `Docker`
- Dockerfile: `free-render/Dockerfile.render`
- Docker build context: `.`
- Plan: `Free`

> Qeyd: Pulsuz versiya məlumatları yaddaşda saxlayır. Render servisi yenidən başladıqda və ya free instansiya yenidən yaradıldıqda mövcud qısa linklər və statistika sıfırlana bilər.

## Lokal işə salma

Tələblər: Docker Desktop.

```bash
docker build -t url-shortener-free -f free-render/Dockerfile.render .
docker run --rm -p 8080:8080 -e PORT=8080 url-shortener-free
```

Sonra [http://localhost:8080](http://localhost:8080) ünvanını açın.

## Texnologiyalar

- Vanilla HTML, CSS və JavaScript
- Java 17 (`HttpServer`)
- Docker
- Render Free Web Service

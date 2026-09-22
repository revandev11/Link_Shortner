# URL Qısaldıcı

Uzun URL-ləri qısaldan, qısa linkləri yönləndirən və istifadə statistikasını göstərən veb tətbiq.

## Canlı demo

Layihə Render üzərində deploy olunub:

[https://link-shortner-ku3n.onrender.com/](https://link-shortner-ku3n.onrender.com/)

## Frontend və backend əlaqəsi

Frontend Vanilla HTML, CSS və JavaScript ilə hazırlanıb. İstifadəçi URL daxil etdikdə frontend `POST /api/urls` sorğusu göndərir, backend qısa kod yaradıb qısa linki geri qaytarır. “Statistikaya bax” düyməsi isə `GET /api/analytics/{shortCode}` endpointi vasitəsilə klik və yaradılma saylarını alır.

Frontend və backend eyni Render servisində çalışdığı üçün API sorğuları eyni domen üzərindən `/api` yolu ilə göndərilir. Bu yanaşma əlavə CORS konfiqurasiyasına ehtiyacı aradan qaldırır. Yaradılan qısa link açıldıqda backend istifadəçini orijinal URL-ə yönləndirir və klik statistikasını yeniləyir.

## İmkanlar

- Tam URL-dən yeddi simvollu qısa kod yaradır.
- Qısa link açıldıqda orijinal ünvana yönləndirir.
- Yaradılma hadisəsi və klik sayını göstərir.
- Linki bir kliklə clipboard-a köçürür.
- Mobil ekranlara uyğun sadə interfeys təqdim edir.

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

## Texnologiyalar
- Java 17
- RabbitMq
- Api-Gateway
- Docker
- SpringBoot
- Swagger UI
- MySql
- Render
- Vanilla HTML
- CSS
- JavaScript

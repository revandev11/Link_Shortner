# Infra — Mikroservis Layihəsi

Bu lahiye mikroservis texnelogiyalarını tədbiq etmək üçün yaranıb.
## Arxitektura

Layihə 4 servisdən ibarətdir:

| Servis | Rolu | Repo |
|---|---|---|
| `discovery-service` | Eureka — servis kəşfi (service discovery) | `api-gateway` reposunda |
| `api-gateway` | Bütün sorğuların giriş nöqtəsi (routing) | `api-gateway` |
| `url-service` | Əsas biznes məntiqi (URL qısaltma) | `url-service` |
| `analytics-service` | Hadisələri (events) qəbul edib analiz edir | `analytics-service` |

Əlavə infrastruktur:
- **MySQL** — `url-service` üçün məlumat bazası
- **RabbitMQ** — `url-service` ilə `analytics-service` arasında asinxron mesajlaşma

## Repo Strukturu

Bu layihə **polyrepo** yanaşması ilə qurulub — hər hissə öz ayrı GitHub reposundadır:

```
url-service/           → Java/Spring Boot kodu, pom.xml, Dockerfile
analytics-service/     → Java/Spring Boot kodu, pom.xml, Dockerfile
api-gateway/           → discovery-service + api-gateway kodu
infra/                 → (bu repo) yalnız docker-compose.yml və CI/CD
```

## Lokal İşə Salma

### Tələblər
- Docker və Docker Compose quraşdırılmış olmalıdır
- Aşağıdakı 4 repo eyni ana qovluğun altına, **yan-yana** klonlanmalıdır:

```
projects/
├── infra/
├── url-service/
├── analytics-service/
└── api-gateway/
```

Bu vacibdir, çünki `docker-compose.yml`-də hər servisin build yolu (`../url-service` kimi) buna əsaslanır.

### Addımlar

```bash
cd infra
docker compose up --build
```

### Portlar

| Servis | Port |
|---|---|
| Eureka (discovery-service) | `8761` |
| API Gateway | `8080` |
| MySQL | `3306` |
| RabbitMQ (AMQP) | `5672` |
| RabbitMQ Management UI | `15672` |

## Texnologiyalar

- Java / Spring Boot
- Spring Cloud Netflix Eureka (service discovery)
- Spring Cloud Gateway
- MySQL
- RabbitMQ
- Docker / Docker Compose

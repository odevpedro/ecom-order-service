# ecom-order-service

> Orquestração central de pedidos para plataforma de e-commerce — coordena o fluxo completo: valida usuário, consulta produtos, calcula frete, processa pagamento e emite nota fiscal.

[![License](https://img.shields.io/github/license/odevpedro/ecom-order-service?style=flat-square)](./LICENSE)
[![Last Commit](https://img.shields.io/github/last-commit/odevpedro/ecom-order-service?style=flat-square)](https://github.com/odevpedro/ecom-order-service/commits/master)

---

## Sobre o Projeto

API REST responsável pela criação e consulta de pedidos. Atua como **orquestrador SAGA** — coordena 5 serviços downstream em sequência para completar um pedido: valida o usuário, busca dados dos produtos, calcula frete, processa pagamento e emite nota fiscal. Cada cliente HTTP possui fallback stub para desenvolvimento sem dependências externas.

Faz parte de um ecossistema **polyglot** de microserviços (Java/Spring Boot, Python, Go, Node.js, TypeScript).

---

## Stack & Arquitetura

| Camada        | Tecnologia                          |
|---------------|--------------------------------------|
| Runtime       | Java 21 (Temurin)                    |
| Framework     | Spring Boot 3.4                      |
| ORM           | Spring Data JPA / Hibernate          |
| Clientes HTTP | RestTemplate + stub fallback         |
| Validação     | Jakarta Validation                   |
| Banco de dados| PostgreSQL 15                        |
| Build         | Maven                                |
| Infra         | Docker + Docker Compose              |
| CI/CD         | GitHub Actions                       |
| Testes        | JUnit 5 + Mockito                    |

> Padrão arquitetural: **SAGA Orchestrator**. O `OrderService` centraliza a orquestração chamando serviços downstream sequencialmente. Cada cliente HTTP encapsula a lógica de fallback stub.

---

## Estrutura de Pastas

```
src/main/java/com/ecom/order/
├── OrderApplication.java                     # @SpringBootApplication
├── controller/
│   ├── OrderController.java                  # POST create, GET by id/user
│   └── HealthController.java                 # /health, /live, /ready
├── service/OrderService.java                 # Orquestrador central
├── model/
│   ├── Order.java                            # JPA Entity
│   ├── OrderItem.java                        # JPA Entity
│   └── OrderStatus.java                      # Enum: PENDING→CONFIRMED→PAID→...
├── repository/OrderRepository.java           # Spring Data JPA
├── dto/                                      # CreateOrderRequest, OrderResponse
├── client/
│   ├── ProductClient.java                    # → product-catalog
│   ├── UserClient.java                       # → user-service
│   ├── ShippingClient.java                   # → shipping-service
│   ├── PaymentClient.java                    # → payment-service
│   └── InvoiceClient.java                    # → invoice-service
├── config/
│   ├── RequestIdFilter.java                  # X-Request-ID
│   └── ErrorResponse.java                    # Erro padronizado
└── exception/
    └── GlobalExceptionHandler.java            # @RestControllerAdvice
```

---

## Como Rodar Localmente

### Pré-requisitos

- Docker + Docker Compose
- JDK 21 + Maven

### Setup

```bash
cp .env.example .env
docker compose up -d postgres-order
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:3003`.

### Variáveis de Ambiente

| Variável                  | Descrição                            | Valor padrão (dev)                                      |
|---------------------------|--------------------------------------|---------------------------------------------------------|
| `PORT`                    | Porta do servidor                    | `3003`                                                  |
| `DATABASE_URL`            | URL JDBC do PostgreSQL               | `jdbc:postgresql://localhost:5432/ecom_order`           |
| `DATABASE_USER`           | Usuário do banco                     | `ecom`                                                  |
| `DATABASE_PASSWORD`       | Senha do banco                       | `ecom`                                                  |
| `PRODUCT_CATALOG_URL`     | URL do Product Catalog                | `http://localhost:3001`                                 |
| `USER_SERVICE_URL`        | URL do User Service                   | `http://localhost:3007`                                 |
| `PAYMENT_SERVICE_URL`     | URL do Payment Service                | `http://localhost:3004`                                 |
| `SHIPPING_SERVICE_URL`    | URL do Shipping Service               | `http://localhost:3005`                                 |
| `INVOICE_SERVICE_URL`     | URL do Invoice Service                | `http://localhost:3006`                                 |
| `SPRING_PROFILES_ACTIVE`  | Perfil ativo do Spring                | `dev`                                                   |

---

## Testes

```bash
./mvnw test
```

**3 cenários:**
| Suite                    | Arquivo                         | Cenários |
|--------------------------|---------------------------------|----------|
| Unitários (OrderService) | `OrderServiceTest.java`         | 3        |

---

## API — Endpoints

| Método | Rota                      | Descrição                    |
|--------|---------------------------|------------------------------|
| GET    | `/health`                 | Health check                 |
| GET    | `/live`                   | Liveness probe               |
| GET    | `/ready`                  | Readiness probe              |
| POST   | `/api/orders`             | Cria pedido (orquestrado)    |
| GET    | `/api/orders/{id}`        | Busca pedido por ID          |
| GET    | `/api/orders?userId=`     | Lista pedidos por usuário    |

> Documentação interativa: `http://localhost:3003/swagger-ui.html` (Springdoc OpenAPI)

---

## Documentação Técnica

| Documento                                        | Descrição                                 |
|--------------------------------------------------|-------------------------------------------|
| [Fluxos de Funcionalidades](./docs/system-feature-flows.md) | Fluxo interno de cada feature |
| [Modelo de Dados](./docs/data-model.md)          | Entidades, relacionamentos e enums        |
| [Backlog](./backlog.md)                          | Status de desenvolvimento                 |

---

## Status do Projeto

```
[x] Criação de pedido com orquestração completa
[x] 5 clientes HTTP com fallback stub
[x] Status lifecycle: PENDING → CONFIRMED
[x] JPA + PostgreSQL com ddl-auto=update
[x] Health checks + Request ID + erro padronizado
[ ] Rollback / compensação em caso de falha
[ ] Circuit breaker nos clientes HTTP
[ ] Filas de retry para falhas temporárias
```

---

## Licença

Distribuído sob a licença MIT. Veja [LICENSE](./LICENSE) para mais informações.

---

<p align="center">
  Feito com foco em qualidade por <a href="https://github.com/odevpedro">@odevpedro</a>
</p>

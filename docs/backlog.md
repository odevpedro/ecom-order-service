# Backlog — ecom-order-service

> Registro vivo do progresso do projeto. Atualizado a cada mudança de estado de uma funcionalidade.
> **Ultima atualizacao:** 2026-06-17

---

## Sobre o Projeto

API REST responsavel pela criacao e consulta de pedidos. Atua como orquestrador SAGA — coordena 5 servicos downstream em sequencia para completar um pedido: valida o usuario, busca dados dos produtos, calcula frete, processa pagamento e emite nota fiscal. Cada cliente HTTP possui fallback stub para desenvolvimento sem dependencias externas.

**Versao atual:** `1.0.0`
**Repositorio:** [github.com/odevpedro/ecom-order-service](https://github.com/odevpedro/ecom-order-service)
**Stack principal:** Java 21, Spring Boot 3.4, PostgreSQL 15, JPA/Hibernate

---

## Legenda

| Simbolo | Significado |
|---------|-------------|
| `[ ]`   | Pendente |
| `[~]`   | Em andamento |
| `[x]`   | Concluido |
| `P0`    | Critico — bloqueia outras features |
| `P1`    | Alta prioridade |
| `P2`    | Media prioridade |
| `P3`    | Melhoria / nice-to-have |
| `XS` `S` `M` `L` `XL` | Estimativa de complexidade |

---

## Em Andamento

> Features atualmente sendo desenvolvidas. Idealmente, maximo de 2–3 itens simultaneos.

| Prioridade | Feature | Descricao | Inicio |
|------------|---------|-----------|--------|
| P3 | Eventos de dominio (mensageria) | Publicar eventos de mudanca de status do pedido via RabbitMQ — exchange `ecom.order`, routing keys `order.created` / `order.confirmed` | 2026-06-17 |

---

## Pendentes

> Ordenadas por prioridade. Itens de P0 e P1 devem entrar em "Em Andamento" primeiro.

Nenhum item pendente no momento.

---

## Concluidas

> Features finalizadas com suas respectivas datas de conclusao e links de referencia.

| Feature | Data | Descricao |
|---------|------|-----------|
| Criacao de pedido com orquestracao completa | 2026-06-16 | POST /api/orders orquestra 5 servicos downstream em sequencia |
| 5 clientes HTTP com fallback stub | 2026-06-16 | ProductClient, UserClient, ShippingClient, PaymentClient, InvoiceClient com stub em caso de falha |
| Status lifecycle PENDING -> CONFIRMED | 2026-06-16 | Ciclo de vida: PENDING -> PAID -> CONFIRMED (3 estados no fluxo de criacao) |
| JPA + PostgreSQL com ddl-auto=update | 2026-06-16 | Entidades Order e OrderItem mapeadas com Hibernate, schema auto-criado |
| Health checks: /health, /live, /ready | 2026-06-16 | Endpoints de sonda para Kubernetes / Docker health |
| Request ID + erro padronizado | 2026-06-16 | Filtro X-Request-ID e envelope de erro unificado (ErrorResponse) |
| Consulta de pedido por ID | 2026-06-16 | GET /api/orders/{id} com tratamento de nao encontrado |
| Listagem de pedidos por usuario | 2026-06-16 | GET /api/orders?userId= com ordenacao por createdAt DESC |
| Validacao de entrada com Jakarta Validation | 2026-06-16 | @NotBlank, @NotEmpty, @Valid nos DTOs de criacao |
| Suite de testes unitarios (3 cenarios) | 2026-06-16 | create, findById, findById not found throws — Mockito + JUnit 5 |
| Circuit breaker nos clientes HTTP | 2026-06-17 | resilience4j com sliding window 10, threshold 50%, half-open 3, wait 10s |
| SAGA compensatoria com rollback | 2026-06-17 | SagaCoordinator com 5 steps, compensacao reversa em caso de falha |
| Retry com backoff nos clientes HTTP | 2026-06-17 | @Retryable com maxAttempts=3, backoff=2s, fallback stub |

---

## Bugs Conhecidos

> Problemas identificados que ainda nao foram corrigidos.

Nenhum bug conhecido no momento.

---

## Notas & Decisoes Pendentes

> Pontos em aberto que precisam de decisao antes de serem desenvolvidos.

| ID | Decisao | Contexto |
|----|---------|----------|
| DEC-001 | Estrategia de rollback | Definir se o rollback sera sincrono (chamada de compensacao em cada cliente) ou baseado em evento (dead letter queue) |
| DEC-002 | Timeout global vs por cliente | Definir se o timeout de 5s e global ou configurado individualmente por servico downstream |

---

## Historico de Versoes

| Versao | Data | Principais entregas |
|--------|------|---------------------|
| `1.0.0` | 2026-06-16 | Criacao de pedido orquestrada, 5 clientes HTTP com stub, health checks, consultas JPA |

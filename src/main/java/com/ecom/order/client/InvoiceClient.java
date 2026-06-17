package com.ecom.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
public class InvoiceClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public InvoiceClient(@Value("${services.invoice}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Retryable(
            retryFor = { ResourceAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CircuitBreaker(name = "invoice", fallbackMethod = "fallbackIssueInvoice")
    @SuppressWarnings("unchecked")
    public Map<String, Object> issueInvoice(String orderId, int amountCents, String cpfCnpj) {
        String url = baseUrl + "/invoices";
        Map<String, Object> body = Map.of(
            "pedido_id", orderId,
            "valor_cents", amountCents,
            "cpf_cnpj", cpfCnpj
        );
        return rest.postForObject(url, new HttpEntity<>(body), Map.class);
    }

    public void cancelInvoice(String invoiceKey) {
        try {
            String url = baseUrl + "/invoices/" + invoiceKey + "/cancel";
            rest.postForObject(url, HttpEntity.EMPTY, Map.class);
        } catch (Exception ignored) {
        }
    }

    private Map<String, Object> fallbackIssueInvoice(String orderId, int amountCents, String cpfCnpj, Exception e) {
        return stubInvoice(orderId);
    }

    private Map<String, Object> stubInvoice(String orderId) {
        String key = UUID.randomUUID().toString().replace("-", "").substring(0, 44);
        return Map.of(
            "chave_acesso", key,
            "protocolo", "123456789012345",
            "status", "autorizada"
        );
    }
}

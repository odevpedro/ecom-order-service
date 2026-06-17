package com.ecom.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ProductClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public ProductClient(@Value("${services.product-catalog}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Retryable(
            retryFor = { ResourceAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackGetProduct")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getProduct(String productId) {
        String url = baseUrl + "/api/products/" + productId;
        return rest.getForObject(url, Map.class);
    }

    private Map<String, Object> fallbackGetProduct(String productId, Exception e) {
        return stubProduct(productId);
    }

    private Map<String, Object> stubProduct(String productId) {
        return Map.of(
            "id", productId,
            "name", "Stub Product",
            "priceCents", 1000,
            "sku", "STUB-" + productId.substring(0, 8),
            "stockQuantity", 10
        );
    }
}

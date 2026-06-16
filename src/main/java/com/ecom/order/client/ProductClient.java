package com.ecom.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    @SuppressWarnings("unchecked")
    public Map<String, Object> getProduct(String productId) {
        try {
            String url = baseUrl + "/api/products/" + productId;
            return rest.getForObject(url, Map.class);
        } catch (Exception e) {
            return stubProduct(productId);
        }
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

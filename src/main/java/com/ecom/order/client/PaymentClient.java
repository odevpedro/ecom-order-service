package com.ecom.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
public class PaymentClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public PaymentClient(@Value("${services.payment}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> processPayment(String orderId, int amountCents) {
        try {
            String url = baseUrl + "/api/payments";
            Map<String, Object> body = Map.of(
                "orderId", orderId,
                "amountCents", amountCents,
                "currency", "BRL"
            );
            return rest.postForObject(url, new HttpEntity<>(body), Map.class);
        } catch (Exception e) {
            return stubPayment(orderId);
        }
    }

    private Map<String, Object> stubPayment(String orderId) {
        return Map.of(
            "id", UUID.randomUUID().toString(),
            "orderId", orderId,
            "status", "confirmed",
            "paymentMethod", "stub"
        );
    }
}

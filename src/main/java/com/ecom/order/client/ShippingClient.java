package com.ecom.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ShippingClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public ShippingClient(@Value("${services.shipping}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> calculateShipping(String fromCep, String toCep, double weightKg) {
        try {
            String url = baseUrl + "/api/shipping/calculate";
            Map<String, Object> body = Map.of(
                "from_cep", fromCep, "to_cep", toCep,
                "weight_kg", weightKg, "height_cm", 10,
                "width_cm", 10, "length_cm", 10
            );
            return rest.postForObject(url, new HttpEntity<>(body), Map.class);
        } catch (Exception e) {
            return stubShipping(weightKg);
        }
    }

    private Map<String, Object> stubShipping(double weightKg) {
        return Map.of(
            "carrier", "Stub Carrier",
            "service_name", "Standard",
            "price_cents", (int) (weightKg * 100),
            "estimated_days", 5,
            "currency", "BRL"
        );
    }
}

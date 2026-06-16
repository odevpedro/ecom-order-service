package com.ecom.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UserClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public UserClient(@Value("${services.user}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUser(String userId) {
        try {
            String url = baseUrl + "/api/users/" + userId;
            return rest.getForObject(url, Map.class);
        } catch (Exception e) {
            return stubUser(userId);
        }
    }

    private Map<String, Object> stubUser(String userId) {
        return Map.of("id", userId, "name", "Stub User", "email", "stub@email.com");
    }
}

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
public class UserClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public UserClient(@Value("${services.user}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Retryable(
            retryFor = { ResourceAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackGetUser")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUser(String userId) {
        String url = baseUrl + "/api/users/" + userId;
        return rest.getForObject(url, Map.class);
    }

    private Map<String, Object> fallbackGetUser(String userId, Exception e) {
        return stubUser(userId);
    }

    private Map<String, Object> stubUser(String userId) {
        return Map.of("id", userId, "name", "Stub User", "email", "stub@email.com");
    }
}

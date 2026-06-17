package com.ecom.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok", "service", "order");
    }

    @GetMapping("/live")
    public Map<String, Object> live() {
        return Map.of("status", "alive");
    }

    @GetMapping("/ready")
    public Map<String, Object> ready() {
        return Map.of("status", "ready");
    }
}

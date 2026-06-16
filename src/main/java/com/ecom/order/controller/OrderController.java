package com.ecom.order.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public String create() {
        return "create - not implemented";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable String id) {
        return "getById - not implemented";
    }

    @GetMapping
    public String list() {
        return "list - not implemented";
    }
}

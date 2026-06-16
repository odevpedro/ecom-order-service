package com.ecom.order.controller;

import com.ecom.order.dto.CreateOrderRequest;
import com.ecom.order.dto.OrderResponse;
import com.ecom.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable String id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderResponse> list(@RequestParam String userId) {
        return orderService.listByUser(userId);
    }
}

package com.ecom.order.service;

import com.ecom.order.dto.CreateOrderRequest;
import com.ecom.order.dto.OrderResponse;
import com.ecom.order.model.Order;
import com.ecom.order.model.OrderStatus;
import com.ecom.order.repository.OrderRepository;
import com.ecom.order.service.saga.OrderContext;
import com.ecom.order.service.saga.SagaCoordinator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final SagaCoordinator sagaCoordinator;

    public OrderService(OrderRepository orderRepository,
                        SagaCoordinator sagaCoordinator) {
        this.orderRepository = orderRepository;
        this.sagaCoordinator = sagaCoordinator;
    }

    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStreet(request.getStreet());
        order.setNumber(request.getNumber());
        order.setNeighborhood(request.getNeighborhood());
        order.setCity(request.getCity());
        order.setState(request.getState());
        order.setZipCode(request.getZipCode());

        OrderContext context = new OrderContext(request, order);

        sagaCoordinator.execute(context);

        order.setTotalCents(context.getTotalCents());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTrackingCode("TRACK" + order.getId().substring(0, 12).toUpperCase());

        Order saved = orderRepository.save(order);
        return OrderResponse.fromEntity(saved);
    }

    public OrderResponse findById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderResponse.fromEntity(order);
    }

    public List<OrderResponse> listByUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(OrderResponse::fromEntity).collect(Collectors.toList());
    }
}

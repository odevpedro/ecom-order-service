package com.ecom.order.service;

import com.ecom.order.client.*;
import com.ecom.order.dto.CreateOrderRequest;
import com.ecom.order.dto.OrderResponse;
import com.ecom.order.model.Order;
import com.ecom.order.model.OrderItem;
import com.ecom.order.model.OrderStatus;
import com.ecom.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final ShippingClient shippingClient;
    private final PaymentClient paymentClient;
    private final InvoiceClient invoiceClient;

    public OrderService(OrderRepository orderRepository,
                        ProductClient productClient,
                        UserClient userClient,
                        ShippingClient shippingClient,
                        PaymentClient paymentClient,
                        InvoiceClient invoiceClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
        this.shippingClient = shippingClient;
        this.paymentClient = paymentClient;
        this.invoiceClient = invoiceClient;
    }

    public OrderResponse create(CreateOrderRequest request) {
        userClient.getUser(request.getUserId());

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStreet(request.getStreet());
        order.setNumber(request.getNumber());
        order.setNeighborhood(request.getNeighborhood());
        order.setCity(request.getCity());
        order.setState(request.getState());
        order.setZipCode(request.getZipCode());

        int totalCents = 0;
        double totalKg = 0;

        for (CreateOrderRequest.ItemRequest itemReq : request.getItems()) {
            Map<String, Object> product = productClient.getProduct(itemReq.getProductId());
            int price = (int) product.getOrDefault("priceCents", 0);
            int qty = itemReq.getQuantity();

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(itemReq.getProductId());
            item.setSku(itemReq.getSku());
            item.setName((String) product.getOrDefault("name", "Unknown"));
            item.setQuantity(qty);
            item.setUnitPriceCents(price);

            order.getItems().add(item);
            totalCents += price * qty;
            totalKg += qty * 0.5;
        }

        Map<String, Object> shipping = shippingClient.calculateShipping(
                "01001000", request.getZipCode(), totalKg);
        int shippingCost = (int) shipping.getOrDefault("price_cents", 0);
        order.setShippingCostCents(shippingCost);
        totalCents += shippingCost;
        order.setTotalCents(totalCents);

        Map<String, Object> payment = paymentClient.processPayment(order.getId(), totalCents);
        order.setPaymentId((String) payment.get("id"));
        order.setStatus(OrderStatus.PAID);

        Map<String, Object> invoice = invoiceClient.issueInvoice(order.getId(), totalCents, "00000000000");
        order.setInvoiceKey((String) invoice.get("chave_acesso"));
        order.setTrackingCode("TRACK" + order.getId().substring(0, 12).toUpperCase());

        order.setStatus(OrderStatus.CONFIRMED);

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

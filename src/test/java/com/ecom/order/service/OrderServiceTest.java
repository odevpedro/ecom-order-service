package com.ecom.order.service;

import com.ecom.order.client.*;
import com.ecom.order.dto.CreateOrderRequest;
import com.ecom.order.dto.OrderResponse;
import com.ecom.order.messaging.OrderEventPublisher;
import com.ecom.order.model.Order;
import com.ecom.order.model.OrderStatus;
import com.ecom.order.repository.OrderRepository;
import com.ecom.order.service.saga.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductClient productClient;
    @Mock
    private UserClient userClient;
    @Mock
    private ShippingClient shippingClient;
    @Mock
    private PaymentClient paymentClient;
    @Mock
    private InvoiceClient invoiceClient;
    @Mock
    private OrderEventPublisher eventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        List<SagaStep> steps = List.of(
                new ValidateUserStep(userClient),
                new FetchProductsStep(productClient),
                new CalculateShippingStep(shippingClient),
                new ProcessPaymentStep(paymentClient),
                new IssueInvoiceStep(invoiceClient)
        );
        SagaCoordinator sagaCoordinator = new SagaCoordinator(steps);
        orderService = new OrderService(orderRepository, sagaCoordinator, eventPublisher);
    }

    @Test
    void createOrderPersistsAndReturnsResponse() {
        when(userClient.getUser(any())).thenReturn(Map.of("id", "user-1"));
        when(productClient.getProduct(any())).thenReturn(
                Map.of("id", "prod-1", "name", "Product", "priceCents", 1000, "sku", "SKU-1"));
        when(shippingClient.calculateShipping(any(), any(), anyDouble()))
                .thenReturn(Map.of("price_cents", 500, "carrier", "Stub"));
        when(paymentClient.processPayment(any(), anyInt()))
                .thenReturn(Map.of("id", "pay-1", "status", "confirmed"));
        when(invoiceClient.issueInvoice(any(), anyInt(), any()))
                .thenReturn(Map.of("chave_acesso", "key-123", "protocolo", "prot-1"));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var request = new CreateOrderRequest();
        request.setUserId("user-1");
        request.setStreet("Rua A");
        request.setNumber("100");
        request.setCity("SP");
        request.setState("SP");
        request.setZipCode("01001000");

        var item = new CreateOrderRequest.ItemRequest();
        item.setProductId("prod-1");
        item.setSku("SKU-1");
        item.setQuantity(2);
        request.setItems(List.of(item));

        OrderResponse response = orderService.create(request);

        assertNotNull(response.getId());
        assertEquals("user-1", response.getUserId());
        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        assertTrue(response.getTotalCents() > 0);

        verify(orderRepository).save(any());
    }

    @Test
    void findByIdReturnsOrder() {
        Order order = new Order();
        order.setUserId("user-1");
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));

        OrderResponse response = orderService.findById("order-1");
        assertEquals("user-1", response.getUserId());
    }

    @Test
    void findByIdThrowsWhenNotFound() {
        when(orderRepository.findById("invalid")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> orderService.findById("invalid"));
    }
}

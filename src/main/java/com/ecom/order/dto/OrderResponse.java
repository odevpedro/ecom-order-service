package com.ecom.order.dto;

import com.ecom.order.model.Order;
import com.ecom.order.model.OrderItem;
import com.ecom.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private String id;
    private String userId;
    private OrderStatus status;
    private List<ItemResponse> items;
    private int totalCents;
    private int shippingCostCents;
    private String paymentId;
    private String invoiceKey;
    private String trackingCode;
    private LocalDateTime createdAt;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse r = new OrderResponse();
        r.id = order.getId();
        r.userId = order.getUserId();
        r.status = order.getStatus();
        r.items = order.getItems().stream().map(ItemResponse::fromEntity).collect(Collectors.toList());
        r.totalCents = order.getTotalCents();
        r.shippingCostCents = order.getShippingCostCents();
        r.paymentId = order.getPaymentId();
        r.invoiceKey = order.getInvoiceKey();
        r.trackingCode = order.getTrackingCode();
        r.createdAt = order.getCreatedAt();
        return r;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public OrderStatus getStatus() { return status; }
    public List<ItemResponse> getItems() { return items; }
    public int getTotalCents() { return totalCents; }
    public int getShippingCostCents() { return shippingCostCents; }
    public String getPaymentId() { return paymentId; }
    public String getInvoiceKey() { return invoiceKey; }
    public String getTrackingCode() { return trackingCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class ItemResponse {
        private String productId;
        private String sku;
        private String name;
        private int quantity;
        private int unitPriceCents;

        static ItemResponse fromEntity(OrderItem item) {
            ItemResponse r = new ItemResponse();
            r.productId = item.getProductId();
            r.sku = item.getSku();
            r.name = item.getName();
            r.quantity = item.getQuantity();
            r.unitPriceCents = item.getUnitPriceCents();
            return r;
        }

        public String getProductId() { return productId; }
        public String getSku() { return sku; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public int getUnitPriceCents() { return unitPriceCents; }
    }
}

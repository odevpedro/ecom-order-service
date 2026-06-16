package com.ecom.order.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    private String id;

    @Column(nullable = false, length = 36)
    private String orderId;

    @Column(nullable = false, length = 36)
    private String productId;

    @Column(nullable = false, length = 100)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int unitPriceCents;

    public OrderItem() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getUnitPriceCents() { return unitPriceCents; }
    public void setUnitPriceCents(int unitPriceCents) { this.unitPriceCents = unitPriceCents; }
}

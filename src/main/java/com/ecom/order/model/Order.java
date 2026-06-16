package com.ecom.order.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    @Column(nullable = false, length = 36)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orderId")
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private int totalCents;

    @Column(nullable = false)
    private int shippingCostCents;

    @Column(length = 255)
    private String street;

    @Column(length = 20)
    private String number;

    @Column(length = 255)
    private String neighborhood;

    @Column(length = 255)
    private String city;

    @Column(length = 2)
    private String state;

    @Column(length = 9)
    private String zipCode;

    @Column(length = 100)
    private String paymentId;

    @Column(length = 44)
    private String invoiceKey;

    @Column(length = 100)
    private String trackingCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; this.updatedAt = LocalDateTime.now(); }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public int getTotalCents() { return totalCents; }
    public void setTotalCents(int totalCents) { this.totalCents = totalCents; }
    public int getShippingCostCents() { return shippingCostCents; }
    public void setShippingCostCents(int shippingCostCents) { this.shippingCostCents = shippingCostCents; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getInvoiceKey() { return invoiceKey; }
    public void setInvoiceKey(String invoiceKey) { this.invoiceKey = invoiceKey; }
    public String getTrackingCode() { return trackingCode; }
    public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

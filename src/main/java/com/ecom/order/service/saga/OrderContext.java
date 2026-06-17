package com.ecom.order.service.saga;

import com.ecom.order.dto.CreateOrderRequest;
import com.ecom.order.model.Order;

import java.util.Map;

public class OrderContext {

    private final CreateOrderRequest request;
    private final Order order;
    private int totalCents;
    private double totalKg;
    private Map<String, Object> userData;
    private Map<String, Object> shippingData;
    private Map<String, Object> paymentData;
    private Map<String, Object> invoiceData;

    public OrderContext(CreateOrderRequest request, Order order) {
        this.request = request;
        this.order = order;
    }

    public CreateOrderRequest getRequest() { return request; }
    public Order getOrder() { return order; }
    public int getTotalCents() { return totalCents; }
    public void setTotalCents(int totalCents) { this.totalCents = totalCents; }
    public double getTotalKg() { return totalKg; }
    public void setTotalKg(double totalKg) { this.totalKg = totalKg; }
    public Map<String, Object> getUserData() { return userData; }
    public void setUserData(Map<String, Object> userData) { this.userData = userData; }
    public Map<String, Object> getShippingData() { return shippingData; }
    public void setShippingData(Map<String, Object> shippingData) { this.shippingData = shippingData; }
    public Map<String, Object> getPaymentData() { return paymentData; }
    public void setPaymentData(Map<String, Object> paymentData) { this.paymentData = paymentData; }
    public Map<String, Object> getInvoiceData() { return invoiceData; }
    public void setInvoiceData(Map<String, Object> invoiceData) { this.invoiceData = invoiceData; }
}

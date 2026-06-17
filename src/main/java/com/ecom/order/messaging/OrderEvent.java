package com.ecom.order.messaging;

import java.time.LocalDateTime;

public class OrderEvent {

    private String eventType;
    private String orderId;
    private String userId;
    private int totalCents;
    private String timestamp;

    public OrderEvent() {}

    public OrderEvent(String eventType, String orderId, String userId, int totalCents) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.userId = userId;
        this.totalCents = totalCents;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getTotalCents() { return totalCents; }
    public void setTotalCents(int totalCents) { this.totalCents = totalCents; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}

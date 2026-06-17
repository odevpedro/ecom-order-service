package com.ecom.order.service.saga;

public interface SagaStep {
    void execute(OrderContext context);
    void compensate(OrderContext context);
}

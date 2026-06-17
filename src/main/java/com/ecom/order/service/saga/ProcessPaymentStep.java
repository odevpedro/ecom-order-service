package com.ecom.order.service.saga;

import com.ecom.order.client.PaymentClient;
import com.ecom.order.model.OrderStatus;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(4)
@Component
public class ProcessPaymentStep implements SagaStep {

    private final PaymentClient paymentClient;

    public ProcessPaymentStep(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Override
    public void execute(OrderContext context) {
        Map<String, Object> paymentData = paymentClient.processPayment(
                context.getOrder().getId(), context.getTotalCents());
        context.getOrder().setPaymentId((String) paymentData.get("id"));
        context.getOrder().setStatus(OrderStatus.PAID);
        context.setPaymentData(paymentData);
    }

    @Override
    public void compensate(OrderContext context) {
        if (context.getOrder().getPaymentId() != null) {
            paymentClient.refundPayment(context.getOrder().getPaymentId());
        }
    }
}

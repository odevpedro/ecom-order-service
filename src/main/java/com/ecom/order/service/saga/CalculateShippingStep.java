package com.ecom.order.service.saga;

import com.ecom.order.client.ShippingClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(3)
@Component
public class CalculateShippingStep implements SagaStep {

    private final ShippingClient shippingClient;

    public CalculateShippingStep(ShippingClient shippingClient) {
        this.shippingClient = shippingClient;
    }

    @Override
    public void execute(OrderContext context) {
        Map<String, Object> shippingData = shippingClient.calculateShipping(
                "01001000", context.getRequest().getZipCode(), context.getTotalKg());
        int shippingCost = (int) shippingData.getOrDefault("price_cents", 0);
        context.getOrder().setShippingCostCents(shippingCost);
        context.setTotalCents(context.getTotalCents() + shippingCost);
        context.setShippingData(shippingData);
    }

    @Override
    public void compensate(OrderContext context) {
    }
}

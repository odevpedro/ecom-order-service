package com.ecom.order.service.saga;

import com.ecom.order.client.ProductClient;
import com.ecom.order.dto.CreateOrderRequest;
import com.ecom.order.model.OrderItem;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(2)
@Component
public class FetchProductsStep implements SagaStep {

    private final ProductClient productClient;

    public FetchProductsStep(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public void execute(OrderContext context) {
        com.ecom.order.model.Order order = context.getOrder();
        int totalCents = 0;
        double totalKg = 0;

        for (CreateOrderRequest.ItemRequest itemReq : context.getRequest().getItems()) {
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

        context.setTotalCents(totalCents);
        context.setTotalKg(totalKg);
    }

    @Override
    public void compensate(OrderContext context) {
    }
}

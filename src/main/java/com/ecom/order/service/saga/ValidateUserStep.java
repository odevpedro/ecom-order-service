package com.ecom.order.service.saga;

import com.ecom.order.client.UserClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(1)
@Component
public class ValidateUserStep implements SagaStep {

    private final UserClient userClient;

    public ValidateUserStep(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public void execute(OrderContext context) {
        Map<String, Object> userData = userClient.getUser(context.getRequest().getUserId());
        context.setUserData(userData);
    }

    @Override
    public void compensate(OrderContext context) {
    }
}

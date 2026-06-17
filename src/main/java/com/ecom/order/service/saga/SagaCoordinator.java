package com.ecom.order.service.saga;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SagaCoordinator {

    private final List<SagaStep> steps;

    public SagaCoordinator(List<SagaStep> steps) {
        this.steps = steps;
    }

    public void execute(OrderContext context) {
        List<SagaStep> executed = new ArrayList<>();
        for (SagaStep step : steps) {
            try {
                step.execute(context);
                executed.add(step);
            } catch (Exception e) {
                compensate(executed, context);
                throw new SagaExecutionException("Saga failed at step: " + step.getClass().getSimpleName(), e);
            }
        }
    }

    private void compensate(List<SagaStep> executed, OrderContext context) {
        for (int i = executed.size() - 1; i >= 0; i--) {
            try {
                executed.get(i).compensate(context);
            } catch (Exception ignored) {
            }
        }
    }
}

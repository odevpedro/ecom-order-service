package com.ecom.order.service.saga;

import com.ecom.order.client.InvoiceClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(5)
@Component
public class IssueInvoiceStep implements SagaStep {

    private final InvoiceClient invoiceClient;

    public IssueInvoiceStep(InvoiceClient invoiceClient) {
        this.invoiceClient = invoiceClient;
    }

    @Override
    public void execute(OrderContext context) {
        Map<String, Object> invoiceData = invoiceClient.issueInvoice(
                context.getOrder().getId(), context.getTotalCents(), "00000000000");
        context.getOrder().setInvoiceKey((String) invoiceData.get("chave_acesso"));
        context.setInvoiceData(invoiceData);
    }

    @Override
    public void compensate(OrderContext context) {
        if (context.getOrder().getInvoiceKey() != null) {
            invoiceClient.cancelInvoice(context.getOrder().getInvoiceKey());
        }
    }
}

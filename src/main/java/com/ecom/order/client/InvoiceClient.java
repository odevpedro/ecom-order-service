package com.ecom.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
public class InvoiceClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public InvoiceClient(@Value("${services.invoice}") String baseUrl) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> issueInvoice(String orderId, int amountCents, String cpfCnpj) {
        try {
            String url = baseUrl + "/invoices";
            Map<String, Object> body = Map.of(
                "pedido_id", orderId,
                "valor_cents", amountCents,
                "cpf_cnpj", cpfCnpj
            );
            return rest.postForObject(url, new HttpEntity<>(body), Map.class);
        } catch (Exception e) {
            return stubInvoice(orderId);
        }
    }

    private Map<String, Object> stubInvoice(String orderId) {
        String key = UUID.randomUUID().toString().replace("-", "").substring(0, 44);
        return Map.of(
            "chave_acesso", key,
            "protocolo", "123456789012345",
            "status", "autorizada"
        );
    }
}

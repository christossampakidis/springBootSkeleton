package com.demoapp.demoapp.service;

import java.util.HashMap;
import java.util.Map;

import com.demoapp.demoapp.integration.kafka.Kafka;
import com.demoapp.demoapp.service.interfaces.PaymentProvider;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.integration.stripe.StripeClient;
import com.demoapp.demoapp.model.request.InvoiceRequest;
import com.demoapp.demoapp.model.request.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;

@Service("stripe")
public class StripeService implements PaymentProvider {

    private final StripeClient stripeClient;
    private final Kafka kafkaService;

    public StripeService(StripeClient stripeClient, Kafka kafkaService) {
        this.kafkaService = kafkaService;
        this.stripeClient = stripeClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInvoice(InvoiceRequest invoiceRequest) throws Exception {
        Customer customer = stripeClient.createCustomer(invoiceRequest.email());
        Invoice invoice = stripeClient.createInvoice(customer);
        for (ItemDTO item : invoiceRequest.items()) {
            stripeClient.createInvoiceItem(customer, invoice, item);
        }
        stripeClient.sendInvoice(invoice);
        kafkaService.sendMessage("invoices", "hello from StripeService");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void voidInvoice(Long invoiceId) throws Exception {
        stripeClient.voidInvoice(invoiceId.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> createPaymentIntent(PaymentIntentRequest paymentIntentRequest) throws Exception {
        Map<String, String> response = new HashMap<>();
        var paymentIntentResult = stripeClient.createPaymentIntent(paymentIntentRequest);
        response.put("client_secret", paymentIntentResult.getClientSecret());
        if (paymentIntentResult.getClientSecret() != null) {
            response.put("customerSessionClientSecret", paymentIntentResult.getClientSecret());
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createConnectAccount() throws Exception {
        return stripeClient.createConnectAccount().getClientSecret();
    }
}

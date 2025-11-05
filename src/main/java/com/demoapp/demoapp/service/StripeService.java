package com.demoapp.demoapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.demoapp.demoapp.integration.Stripe.StripeClient;
import com.demoapp.demoapp.model.request.InvoiceRequest;
import com.demoapp.demoapp.model.request.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;

@Service("stripe")
public class StripeService implements PaymentProvider {

    private final StripeClient stripeClient;

    public StripeService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @Override
    public void processInvoice(InvoiceRequest invoiceRequest) throws Exception {
        Customer customer = stripeClient.createCustomer(invoiceRequest.getEmail());
        Invoice invoice = stripeClient.createInvoice(customer);
        for (ItemDTO item : invoiceRequest.getItems()) {
            stripeClient.createInvoiceItem(customer, invoice, item);
        }
        stripeClient.sendInvoice(invoice);
    }

    @Override
    public void voidInvoice(Long invoiceId) throws Exception {
        stripeClient.voidInvoice(invoiceId.toString());
    }

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
}

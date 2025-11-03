package com.demoapp.demoapp.interfaces;

import java.util.Map;

import com.demoapp.demoapp.models.requests.InvoiceRequest;
import com.demoapp.demoapp.models.requests.PaymentIntentRequest;

public interface PaymentProvider {
    void processInvoice(InvoiceRequest invoiceRequest) throws Exception;

    Map<String, String> createPaymentIntent(PaymentIntentRequest paymentIntentRequest) throws Exception;
}
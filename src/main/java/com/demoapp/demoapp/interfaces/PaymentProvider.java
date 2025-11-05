package com.demoapp.demoapp.interfaces;

import java.util.Map;

import com.demoapp.demoapp.models.requests.InvoiceRequest;
import com.demoapp.demoapp.models.requests.PaymentIntentRequest;

public interface PaymentProvider {
    /**
     * Processes an invoice based on the provided invoice request.
     * @param invoiceRequest
     * @throws Exception
     */
    void processInvoice(InvoiceRequest invoiceRequest) throws Exception;

    /**
     * Voids an invoice by its ID.
     * @param invoiceId
     * @throws Exception
     */
    void voidInvoice(Long invoiceId) throws Exception;

    /**
     * Creates a payment intent.
     * @param paymentIntentRequest
     * @return
     * @throws Exception
     */
    Map<String, String> createPaymentIntent(PaymentIntentRequest paymentIntentRequest) throws Exception;
}
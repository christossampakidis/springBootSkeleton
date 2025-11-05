package com.demoapp.demoapp.service;

import java.util.Map;

import com.demoapp.demoapp.model.request.InvoiceRequest;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;

public interface PaymentProvider {

    /**
     * Processes an invoice based on the provided invoice request.
     * 
     * @param invoiceRequest
     * @throws Exception
     */
    void processInvoice(InvoiceRequest invoiceRequest) throws Exception;

    /**
     * Voids an invoice by its ID.
     * 
     * @param invoiceId
     * @throws Exception
     */
    void voidInvoice(Long invoiceId) throws Exception;

    /**
     * Creates a payment intent.
     * 
     * @param paymentIntentRequest
     * @return
     * @throws Exception
     */
    Map<String, String> createPaymentIntent(PaymentIntentRequest paymentIntentRequest) throws Exception;
}
package com.demoapp.demoapp.service.interfaces;

import java.util.Map;

import com.demoapp.demoapp.model.request.InvoiceRequest;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;

public interface PaymentProvider {

    /**
     * Processes an invoice based on the provided invoice request.
     * 
     * @param invoiceRequest the {@link InvoiceRequest} containing invoice
     *        details
     * @throws Exception if an error occurs during processing
     */
    void processInvoice(InvoiceRequest invoiceRequest) throws Exception;

    /**
     * Voids an invoice by its ID.
     * 
     * @param invoiceId the {@link Long ID} of the invoice to void
     * @throws Exception if an error occurs during voiding
     */
    void voidInvoice(Long invoiceId) throws Exception;

    /**
     * Creates a payment intent.
     * 
     * @param paymentIntentRequest the {@link PaymentIntentRequest} containing
     *        payment intent details
     * @return a {@link Map} with payment intent information
     * @throws Exception if an error occurs during creation
     */
    Map<String, String> createPaymentIntent(
            PaymentIntentRequest paymentIntentRequest) throws Exception;
}

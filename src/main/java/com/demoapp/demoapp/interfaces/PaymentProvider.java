package com.demoapp.demoapp.interfaces;

import com.demoapp.demoapp.models.InvoiceRequest;

public interface PaymentProvider {
    void processInvoice(InvoiceRequest invoiceRequest) throws Exception;
}
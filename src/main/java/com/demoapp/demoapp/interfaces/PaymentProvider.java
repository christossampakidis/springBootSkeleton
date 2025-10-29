package com.demoapp.demoapp.interfaces;

import com.demoapp.demoapp.models.InvoiceRequest;

public interface PaymentProvider {
    void createInvoice(InvoiceRequest invoiceRequest) throws Exception;
}
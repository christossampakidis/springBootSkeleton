package com.demoapp.demoapp.integration.Stripe;

import com.demoapp.demoapp.model.request.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;

public interface StripeClient {

    /**
     * Creates or retrieves a Stripe customer by email.
     * 
     * @param email
     * @return
     * @throws Exception
     */
    Customer createCustomer(String email) throws Exception;

    /**
     * 
     * @param providerId
     * @return
     * @throws Exception
     */
    Customer retrieveCustomer(String providerId) throws Exception;

    /**
     * Creates a Stripe invoice for the given customer.
     * 
     * @param customer
     * @return
     * @throws Exception
     */
    Invoice createInvoice(Customer customer) throws Exception;

    /**
     * Sends the invoice to the customer.
     * 
     * @param invoice
     * @throws Exception
     */
    void sendInvoice(Invoice invoice) throws Exception;

    /**
     * Creates an invoice item for the given customer and invoice.
     * 
     * @param customer
     * @param invoice
     * @param item
     * @throws Exception
     */
    void createInvoiceItem(Customer customer, Invoice invoice, ItemDTO item) throws Exception;

    /**
     * Voids a Stripe invoice by its ID.
     * 
     * @param invoiceId
     * @throws Exception
     */
    void voidInvoice(String invoiceId) throws Exception;

    /**
     * Creates a Stripe payment intent based on the provided request.
     * 
     * @param request
     * @return
     * @throws Exception
     */
    PaymentIntent createPaymentIntent(PaymentIntentRequest request) throws Exception;
}
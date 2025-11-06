package com.demoapp.demoapp.integration.stripe;

import com.demoapp.demoapp.model.request.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;

public interface StripeClient {

    /**
     * Creates or retrieves a Stripe customer by email.
     * 
     * @param email the {@link String email}  of the customer
     * @return the Stripe {@link Customer}  object
     * @throws Exception if an error occurs during creation or retrieval
     */
    Customer createCustomer(String email) throws Exception;

    /**
     * 
     * @param providerId the provider {@link String ID}  of the customer
     * @return the Stripe {@link Customer}  object
     * @throws Exception if an error occurs during retrieval
     */
    Customer retrieveCustomer(String providerId) throws Exception;

    /**
     * Creates a Stripe invoice for the given customer.
     * 
     * @param customer the Stripe {@link Customer}  object
     * @return the Stripe {@link Invoice} object
     * @throws Exception if an error occurs during creation
     */
    Invoice createInvoice(Customer customer) throws Exception;

    /**
     * Sends the invoice to the customer.
     * 
     * @param invoice the Stripe {@link Invoice} object
     * @throws Exception if an error occurs during sending
     */
    void sendInvoice(Invoice invoice) throws Exception;

    /**
     * Creates an invoice item for the given customer and invoice.
     * 
     * @param customer the Stripe {@link Customer} object
     * @param invoice the Stripe {@link Invoice} object
     * @param item the {@link ItemDTO} details
     * @throws Exception if an error occurs during creation
     */
    void createInvoiceItem(Customer customer, Invoice invoice, ItemDTO item) throws Exception;

    /**
     * Voids a Stripe invoice by its ID.
     * 
     * @param invoiceId the {@link String ID} of the invoice to void
     * @throws Exception if an error occurs during voiding
     */
    void voidInvoice(String invoiceId) throws Exception;

    /**
     * Creates a Stripe payment intent based on the provided request.
     * 
     * @param request the {@link PaymentIntentRequest PaymentIntentRequest} details
     * @return the Stripe {@link PaymentIntent PaymentIntent}  object
     * @throws Exception if an error occurs during creation
     */
    PaymentIntent createPaymentIntent(PaymentIntentRequest request) throws Exception;
}
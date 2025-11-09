package com.demoapp.demoapp.service.interfaces;

import com.demoapp.demoapp.model.dto.InvoiceDTO;
import com.stripe.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoicesService {

    /**
     * Handles Stripe webhook events related to invoices.
     *
     * @param event the Stripe {@link Event}
     */
    void handleEvent(Event event);

    /**
     * Fetches all invoices and maps them to InvoiceDTOs.
     *
     * @return {@link List } containing {@link InvoiceDTO}
     */
    Page<InvoiceDTO> fetchInvoices(int page, int size);

    /**
     * Updates or creates a StripeInvoice entity based on the provided Stripe
     * Invoice object.
     *
     * @param object the Stripe {@link com.stripe.model.Invoice} object
     */
    void updateInvoice(com.stripe.model.Invoice object);

}

package com.demoapp.demoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entity.StripeCustomer;
import com.demoapp.demoapp.entity.StripeInvoice;
import com.demoapp.demoapp.model.dto.InvoiceDTO;
import com.demoapp.demoapp.repository.CustomerRepository;
import com.demoapp.demoapp.repository.InvoiceRepository;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

@Service
public class InvoicesService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    public InvoicesService(InvoiceRepository invoiceRepository, CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Handles Stripe webhook events related to invoices.
     * 
     * @param event the Stripe {@link Event}
     */
    public void handleEvent(Event event) {
        switch (event.getType()) {
            case "invoice.updated", "invoice.created", "invoice.finalized" -> {
                Optional<StripeObject> deserializer = event.getDataObjectDeserializer().getObject();
                if (deserializer.isPresent() && deserializer.get() instanceof com.stripe.model.Invoice stripeInvoice) {
                    this.updateInvoice(stripeInvoice);
                }
            }
            default -> System.out.println("Unhandled event type: " + event.getType());
        }
    }

    /**
     * Fetches all invoices and maps them to InvoiceDTOs.
     * 
     * @return {@link List } containing {@link InvoiceDTO}
     */
    public List<InvoiceDTO> fetchInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> new InvoiceDTO(
                        invoice.getId(),
                        invoice.getStatus(),
                        invoice.getCustomer().getEmail(),
                        invoice.getInvoiceNumber(),
                        invoice.getCreatedAt(),
                        invoice.getDaysExpire()))
                .toList();
    }

    /**
     * Updates or creates a StripeInvoice entity based on the provided Stripe
     * Invoice object.
     * 
     * @param object the Stripe {@link com.stripe.model.Invoice} object
     */
    public void updateInvoice(com.stripe.model.Invoice object) {
        Optional<StripeInvoice> invoice = invoiceRepository.findByProviderId(object.getId());
        if (invoice.isPresent()) {
            StripeInvoice existingInvoice = invoice.get();
            existingInvoice.setInvoiceNumber(object.getNumber());
            existingInvoice.setStatus(object.getStatus());
            existingInvoice.setAmountDue(object.getAmountDue());
            existingInvoice.setAmountPaid(object.getAmountPaid());
            existingInvoice.setAmountRemaining(object.getAmountRemaining());
            existingInvoice.setAmountShipping(object.getAmountShipping());
            existingInvoice.setSubtotal(object.getSubtotal());
            existingInvoice.setSubtotalExcludingTax(object.getSubtotalExcludingTax());
            existingInvoice.setTotal(object.getTotal());
            existingInvoice.setTotalExcludingTax(object.getTotalExcludingTax());
            existingInvoice.setBillingReason(object.getBillingReason());
            existingInvoice.setDaysExpire(object.getDueDate());
            existingInvoice.setMetadata(object.toJson());
            invoiceRepository.save(existingInvoice);
        } else {
            StripeCustomer customer = customerRepository.findByProviderId(object.getCustomer()).orElseThrow();
            StripeInvoice createdInvoice = new StripeInvoice(
                    customer,
                    object.getNumber(),
                    object.getStatus(),
                    object.getId(),
                    object.getAmountDue(),
                    object.getAmountPaid(),
                    object.getAmountRemaining(),
                    object.getAmountShipping(),
                    object.getSubtotal(),
                    object.getSubtotalExcludingTax(),
                    object.getTotal(),
                    object.getTotalExcludingTax(),
                    object.getBillingReason(),
                    object.getDueDate(),
                    object.toJson());
            invoiceRepository.save(createdInvoice);
        }

    }

}

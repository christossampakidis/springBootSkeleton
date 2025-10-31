package com.demoapp.demoapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entities.StripeCustomer;
import com.demoapp.demoapp.entities.StripeInvoice;
import com.demoapp.demoapp.models.dto.InvoiceDTO;
import com.demoapp.demoapp.repositories.CustomerRepository;
import com.demoapp.demoapp.repositories.InvoiceRepository;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

@Service
public class InvoicesService {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    CustomerRepository customerRepository;

    public void handleEvent(Event event) {
        switch (event.getType()) {
            case "invoice.updated":
            case "invoice.created":
            case "invoice.finalized":
                Optional<StripeObject> deserializer = event.getDataObjectDeserializer().getObject();
                if (deserializer.isPresent() && deserializer.get() instanceof com.stripe.model.Invoice stripeInvoice) {
                    this.updateInvoice(stripeInvoice);
                }
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
    }

    public List<InvoiceDTO> fetchInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> new InvoiceDTO(
                        invoice.getId(),
                        invoice.getCustomer().getEmail(),
                        invoice.getInvoiceNumber(),
                        invoice.getCreatedAt(),
                        invoice.getDaysExpire()))
                .toList();
    }

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

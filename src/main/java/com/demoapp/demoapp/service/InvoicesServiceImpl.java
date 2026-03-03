package com.demoapp.demoapp.service;

import java.time.Instant;
import java.util.Optional;

import com.demoapp.demoapp.service.interfaces.InvoicesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entity.StripeCustomer;
import com.demoapp.demoapp.entity.StripeInvoice;
import com.demoapp.demoapp.model.dto.InvoiceDTO;
import com.demoapp.demoapp.repository.CustomerRepository;
import com.demoapp.demoapp.repository.InvoiceRepository;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoicesServiceImpl implements InvoicesService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;


    /**
     *
     * {@inheritDoc}
     */
    @Override
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
     *
     * {@inheritDoc}
     */
    @Override
    public Page<InvoiceDTO> fetchInvoices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return invoiceRepository.findAll(pageable)
                .map(invoice -> new InvoiceDTO(
                        invoice.getId(),
                        invoice.getStatus(),
                        invoice.getCustomer().getEmail(),
                        invoice.getInvoiceNumber(),
                        invoice.getCreatedAt(),
                        Instant.ofEpochSecond(invoice.getDaysExpire())
                        ));
    }


    /**
     *
     * {@inheritDoc}
     */
    @Override
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
            invoiceRepository.save(StripeInvoice.builder()
                    .customer(customer)
                    .invoiceNumber(object.getNumber())
                    .status(object.getStatus())
                    .providerId(object.getId())
                    .amountDue(object.getAmountDue())
                    .amountRemaining(object.getAmountRemaining())
                    .amountShipping(object.getAmountShipping())
                    .subtotal(object.getSubtotal())
                    .subtotalExcludingTax(object.getSubtotalExcludingTax())
                    .total(object.getTotal())
                    .totalExcludingTax(object.getTotalExcludingTax())
                    .billingReason(object.getBillingReason())
                    .daysExpire(object.getDueDate())
                    .metadata(object.toJson())
                    .build());
        }

    }

}

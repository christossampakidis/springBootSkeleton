package com.demoapp.demoapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.models.dto.InvoiceDTO;
import com.demoapp.demoapp.repositories.StripeInvoiceRepository;

@Service
public class InvoicesService {
    @Autowired
    StripeInvoiceRepository stripeInvoiceRepository;

    public List<InvoiceDTO> fetchInvoices() {
        return stripeInvoiceRepository.findAll().stream()
                .map(invoice -> new InvoiceDTO(
                        invoice.getId(),
                        invoice.getCustomer().getEmail(),
                        invoice.getInvoiceNumber(),
                        invoice.getCreatedAt(),
                        invoice.getDaysExpire()))
                .toList();
    }

}

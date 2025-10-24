package com.demoapp.demoapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entities.StripeInvoice;
import com.demoapp.demoapp.repositories.StripeInvoiceRepository;

@Service
public class InvoicesService {
    @Autowired
    StripeInvoiceRepository stripeInvoiceRepository;

    public List<StripeInvoice> fetchInvoices() {
        return stripeInvoiceRepository.findAll();
    }
}

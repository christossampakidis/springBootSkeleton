package com.demoapp.demoapp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.factories.PaymentFactory;
import com.demoapp.demoapp.interfaces.PaymentProvider;
import com.demoapp.demoapp.models.InvoiceRequest;
import com.demoapp.demoapp.services.InvoicesService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentsController {
    final static String SELECTED_PROVIDER = "stripe";

    @Autowired
    InvoicesService invoicesService;

    @Autowired
    private PaymentFactory paymentFactory;

    @GetMapping("/invoices")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getInvoices() {
        try {
            return ResponseEntity.ok(Map.of("message", invoicesService.fetchInvoices()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error creating invoice"));
        }
    }

    @PostMapping("/invoice")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> sendInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            PaymentProvider provider = paymentFactory.getProvider(SELECTED_PROVIDER);
            provider.processInvoice(invoiceRequest);
            return ResponseEntity.ok(Map.of("message", "Invoice created successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error creating invoice"));
        }
    }

}

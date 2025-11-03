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

import com.demoapp.demoapp.interfaces.PaymentProvider;
import com.demoapp.demoapp.models.requests.InvoiceRequest;
import com.demoapp.demoapp.models.requests.PaymentIntentRequest;
import com.demoapp.demoapp.services.InvoicesService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentsController {
    final static String SELECTED_PROVIDER = "stripe";

    @Autowired
    InvoicesService invoicesService;

    @Autowired
    private Map<String, PaymentProvider> paymentProviders;

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
            PaymentProvider provider = paymentProviders.get(SELECTED_PROVIDER);
            provider.processInvoice(invoiceRequest);
            return ResponseEntity.ok(Map.of("message", "Invoice created successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error creating invoice"));
        }
    }

    @PostMapping("/payment-intent")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody PaymentIntentRequest invoiceRequest) {
        try {
            PaymentProvider provider = paymentProviders.get(SELECTED_PROVIDER);
            Map<String, String> response = provider.createPaymentIntent(invoiceRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error creating payment intent"));
        }
    }
}

package com.demoapp.demoapp.controller;

import java.util.Map;

import com.demoapp.demoapp.service.interfaces.InvoicesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.model.request.InvoiceRequest;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.demoapp.demoapp.service.interfaces.PaymentProvider;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentsController {
    final static String SELECTED_PROVIDER = "stripe";

    private final InvoicesService invoicesService;

    private final Map<String, PaymentProvider> paymentProviders;

    public PaymentsController(InvoicesService invoicesService, @Autowired Map<String, PaymentProvider> paymentProviders) {
        this.invoicesService = invoicesService;
        this.paymentProviders = paymentProviders;
    }
    /**
     * Fetches all invoices.
     * 
     * @return {@link ResponseEntity} with invoice data or error message.
     */
    @GetMapping("/invoices")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getInvoices() {
            return ResponseEntity.ok(Map.of("message", invoicesService.fetchInvoices()));
    }

    /**
     * Sends an invoice based on the provided invoice request.
     * 
     * @param invoiceRequest {@link InvoiceRequest} containing invoice details
     * @return {@link ResponseEntity} with success or error message
     */
    @PostMapping("/invoice")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> sendInvoice(@RequestBody @Valid InvoiceRequest invoiceRequest) throws Exception{
            PaymentProvider provider = paymentProviders.get(SELECTED_PROVIDER);
            provider.processInvoice(invoiceRequest);
            return ResponseEntity.ok(Map.of("message", "Invoice created successfully"));
    }

    /**
     * Voids an invoice by its ID.
     * 
     * @param id {@link Long} ID of the invoice to void
     * @return {@link ResponseEntity} with success or error message
     */
    @DeleteMapping("/invoice/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> voidInvoice(@PathVariable("id") Long id) throws Exception {
            PaymentProvider provider = paymentProviders.get(SELECTED_PROVIDER);
            provider.voidInvoice(id);
            return ResponseEntity.ok(Map.of("message", "Invoice voided successfully"));
    }

    /**
     * Creates a payment intent based on the provided payment intent request.
     * 
     * @param invoiceRequest {@link PaymentIntentRequest} containing payment intent details
     * @return {@link ResponseEntity} with payment intent data or error message
     */
    @PostMapping("/payment-intent")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody @Valid PaymentIntentRequest invoiceRequest) throws Exception{
            PaymentProvider provider = paymentProviders.get(SELECTED_PROVIDER);
            Map<String, String> response = provider.createPaymentIntent(invoiceRequest);
            return ResponseEntity.ok(response);
    }
}

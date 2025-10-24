package com.demoapp.demoapp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.models.InvoiceRequest;
import com.demoapp.demoapp.services.StripeService;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = "http://localhost:3000")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/invoice")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> sendInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            stripeService.createInvoice(invoiceRequest);
            return ResponseEntity.ok(Map.of("message", "Invoice created successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error creating invoice"));
        }
    }
}

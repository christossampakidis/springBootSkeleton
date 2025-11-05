package com.demoapp.demoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.services.CustomersService;
import com.demoapp.demoapp.services.InvoicesService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/webhooks")
public class WebHookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    InvoicesService invoicesService;

    @Autowired
    CustomersService customersService;

    /**
     * Handles Stripe webhook events related to invoices.
     * @param request
     * @param payload
     * @param sigHeader
     * @return
     */
    @PostMapping("/invoices")
    public ResponseEntity<String> handleInvoiceEvents(HttpServletRequest request, @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            invoicesService.handleEvent(event);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        return ResponseEntity.ok("");
    }

    /**
     * Handles Stripe webhook events related to customers.
     * @param request
     * @param payload
     * @param sigHeader
     * @return
     */
    @PostMapping("/customers")
    public ResponseEntity<String> handleCustomerEvents(HttpServletRequest request, @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            customersService.handleEvent(event);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        return ResponseEntity.ok("");
    }

}

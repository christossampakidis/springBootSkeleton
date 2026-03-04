package com.demoapp.demoapp.controller;

import com.demoapp.demoapp.service.interfaces.CustomersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.service.InvoicesServiceImpl;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks")
public class WebHookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final InvoicesServiceImpl invoicesService;

    private final CustomersService customersService;

    /**
     * Handles Stripe webhook events related to invoices.
     *
     * @param payload the request {@link String payload}
     * @param sigHeader the {@link String Stripe signature header}
     * @return {@link ResponseEntity} with status
     */
    @PostMapping("/invoices")
    public ResponseEntity<String> handleInvoiceEvents(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader)
            throws SignatureVerificationException {
        Event event;
        event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        invoicesService.handleEvent(event);

        return ResponseEntity.ok("");
    }

    /**
     * Handles Stripe webhook events related to customers.
     *
     * @param payload the request {@link String payload}
     * @param sigHeader the {@link String Stripe signature header}
     * @return {@link ResponseEntity} with status
     */
    @PostMapping("/customers")
    public ResponseEntity<String> handleCustomerEvents(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader)
            throws SignatureVerificationException {
        Event event;
        event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        customersService.handleEvent(event);
        return ResponseEntity.ok("");
    }

}

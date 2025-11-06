package com.demoapp.demoapp.controller;

import com.demoapp.demoapp.service.interfaces.CustomersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.service.InvoicesService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;


@RestController
@RequestMapping("/webhooks")
public class WebHookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final InvoicesService invoicesService;

    private final CustomersService customersService;

    public WebHookController(InvoicesService invoicesService, CustomersService customersService) {
        this.invoicesService = invoicesService;
        this.customersService = customersService;
    }

    /**
     * Handles Stripe webhook events related to invoices.
     *
     * @param payload the request {@link String payload}
     * @param sigHeader the {@link String Stripe signature header}
     * @return {@link ResponseEntity} with status
     */
    @PostMapping("/invoices")
    public ResponseEntity<String> handleInvoiceEvents(@RequestBody String payload,
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
     *
     * @param payload the request {@link String payload}
     * @param sigHeader the {@link String Stripe signature header}
     * @return {@link ResponseEntity} with status
     */
    @PostMapping("/customers")
    public ResponseEntity<String> handleCustomerEvents(@RequestBody String payload,
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

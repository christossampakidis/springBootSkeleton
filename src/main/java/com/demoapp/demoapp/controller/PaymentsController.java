package com.demoapp.demoapp.controller;

import java.util.Map;

import com.demoapp.demoapp.model.dto.InvoiceDTO;
import com.demoapp.demoapp.model.dto.PageDTO;
import com.demoapp.demoapp.service.interfaces.InvoicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.demoapp.demoapp.model.request.InvoiceRequest;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.demoapp.demoapp.service.interfaces.PaymentProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentsController {

        private final InvoicesService invoicesService;

        private final Map<String, PaymentProvider> paymentProviders;

        static final String SELECTED_PROVIDER = "stripe";

        /**
         * Fetches all invoices.
         * 
         * @return {@link ResponseEntity} with invoice data or error message.
         */
        @GetMapping("/invoices")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<PageDTO<InvoiceDTO>> getInvoices(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Page<InvoiceDTO> invoices =
                                invoicesService.fetchInvoices(page, size);
                var dto = new PageDTO<>(invoices.getContent(),
                                invoices.getNumber(), invoices.getSize(),
                                invoices.getTotalElements(),
                                invoices.getTotalPages(), invoices.isLast());
                return ResponseEntity.ok(dto);
        }

        /**
         * Sends an invoice based on the provided invoice request.
         * 
         * @param invoiceRequest {@link InvoiceRequest} containing invoice
         *        details
         * @return {@link ResponseEntity} with success or error message
         */
        @PostMapping("/invoice")
        // @PreAuthorize("isAuthenticated()")
        public ResponseEntity<Map<String, String>> sendInvoice(
                        @RequestBody @Valid InvoiceRequest invoiceRequest)
                        throws Exception {
                PaymentProvider provider =
                                paymentProviders.get(SELECTED_PROVIDER);
                provider.processInvoice(invoiceRequest);
                return ResponseEntity.ok(Map.of("message",
                                "Invoice created successfully"));
        }

        /**
         * Voids an invoice by its ID.
         * 
         * @param id {@link Long} ID of the invoice to void
         * @return {@link ResponseEntity} with success or error message
         */
        @DeleteMapping("/invoice/{id}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<Map<String, String>> voidInvoice(
                        @PathVariable("id") Long id) throws Exception {
                PaymentProvider provider =
                                paymentProviders.get(SELECTED_PROVIDER);
                provider.voidInvoice(id);
                return ResponseEntity.ok(Map.of("message",
                                "Invoice voided successfully"));
        }

        /**
         * Creates a payment intent based on the provided payment intent
         * request.
         * 
         * @param invoiceRequest {@link PaymentIntentRequest} containing payment
         *        intent details
         * @return {@link ResponseEntity} with payment intent data or error
         *         message
         */
        @PostMapping("/payment-intent")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<Map<String, String>> createPaymentIntent(
                        @RequestBody @Valid PaymentIntentRequest invoiceRequest)
                        throws Exception {
                PaymentProvider provider =
                                paymentProviders.get(SELECTED_PROVIDER);
                Map<String, String> response =
                                provider.createPaymentIntent(invoiceRequest);
                return ResponseEntity.ok(response);
        }
}

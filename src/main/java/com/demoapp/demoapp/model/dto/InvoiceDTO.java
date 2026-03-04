package com.demoapp.demoapp.model.dto;

import java.time.Instant;

public record InvoiceDTO(Long id, String status, String customerEmail,
        String invoiceNumber, Instant createdAt, Instant daysExpire) {
}

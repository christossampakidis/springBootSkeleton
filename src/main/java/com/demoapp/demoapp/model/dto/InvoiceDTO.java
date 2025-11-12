package com.demoapp.demoapp.model.dto;

import java.util.Date;

public record InvoiceDTO(
        Long id,
        String status,
        String customerEmail,
        String invoiceNumber,
        Date createdAt,
        Long daysExpire
) { }

package com.demoapp.demoapp.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceDTO {
    private final Long id;
    private final String status;
    private final String customerEmail;
    private final String invoiceNumber;
    private final Date createdAt;
    private final Long daysExpire;


}

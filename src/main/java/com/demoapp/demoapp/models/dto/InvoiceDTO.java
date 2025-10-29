package com.demoapp.demoapp.models.dto;

import java.util.Date;

public class InvoiceDTO {
    private Long id;
    private String customerEmail;
    private String invoiceNumber;
    private Date createdAt;
    private Long daysExpire;

    public InvoiceDTO(Long id, String customerEmail, String invoiceNumber, Date createdAt, Long daysExpire) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.invoiceNumber = invoiceNumber;
        this.createdAt = createdAt;
        this.daysExpire = daysExpire;
    }

    // Getters (and setters if needed)
    public Long getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Long getDaysExpire() {
        return daysExpire;
    }
}

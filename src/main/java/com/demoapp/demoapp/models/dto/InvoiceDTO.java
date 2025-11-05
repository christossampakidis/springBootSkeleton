package com.demoapp.demoapp.models.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceDTO {
    private Long id;
    private String status;
    private String customerEmail;
    private String invoiceNumber;
    private Date createdAt;
    private Long daysExpire;

    public InvoiceDTO(Long id, String status, String customerEmail, String invoiceNumber, Date createdAt,
            Long daysExpire) {
        this.id = id;
        this.status = status;
        this.customerEmail = customerEmail;
        this.invoiceNumber = invoiceNumber;
        this.createdAt = createdAt;
        this.daysExpire = daysExpire;
    }

}

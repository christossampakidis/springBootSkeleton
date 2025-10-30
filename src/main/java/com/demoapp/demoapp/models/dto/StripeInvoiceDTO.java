package com.demoapp.demoapp.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StripeInvoiceDTO {
    private String stripeId;
    private Double amountDue;
    private Double amountPaid;
    private Double amountRemaining;
    private Double amountShipping;
    private Double subTotal;
    private Double subTotalNoTax;
    private Double total;
    private Double totalNoTax;
    private Double totalTax;
    private String currencyString;
    private Long created;
    private String status;

    // Getters (and setters if needed)
}

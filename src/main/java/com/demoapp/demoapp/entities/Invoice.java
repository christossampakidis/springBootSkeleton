package com.demoapp.demoapp.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices", uniqueConstraints = { @UniqueConstraint(columnNames = { "provider_id" }) })
@Getter
@Setter
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "`status`")
    private String status;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "amount_due", precision = 15, scale = 2)
    private Long amountDue;

    @Column(name = "amount_paid", precision = 15, scale = 2)
    private Long amountPaid;

    @Column(name = "amount_remaining", precision = 15, scale = 2)
    private Long amountRemaining;

    @Column(name = "amount_shipping", precision = 15, scale = 2)
    private Long amountShipping;

    @Column(name = "subtotal", precision = 15, scale = 2)
    private Long subtotal;

    @Column(name = "subtotal_excluding_tax", precision = 15, scale = 2)
    private Long subtotalExcludingTax;

    @Column(name = "total", precision = 15, scale = 2)
    private Long total;

    @Column(name = "total_excluding_tax", precision = 15, scale = 2)
    private Long totalExcludingTax;

    @Column(name = "billing_reason")
    private String billingReason;

    @Column(name = "days_expire")
    private Long daysExpire;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Invoice(
            Customer customer,
            String invoiceNumber,
            String status,
            String providerId,
            Long amountDue,
            Long amountPaid,
            Long amountRemaining,
            Long amountShipping,
            Long subtotal,
            Long subtotalExcludingTax,
            Long total,
            Long totalExcludingTax,
            String billingReason,
            Long daysExpire,
            String metadata) {
        this.customer = customer;
        this.invoiceNumber = invoiceNumber;
        this.status = status;
        this.providerId = providerId;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.amountRemaining = amountRemaining;
        this.amountShipping = amountShipping;
        this.subtotal = subtotal;
        this.subtotalExcludingTax = subtotalExcludingTax;
        this.total = total;
        this.totalExcludingTax = totalExcludingTax;
        this.billingReason = billingReason;
        this.daysExpire = daysExpire;
        this.metadata = metadata;
    }

}
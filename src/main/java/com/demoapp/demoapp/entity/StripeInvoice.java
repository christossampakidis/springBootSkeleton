package com.demoapp.demoapp.entity;

import java.time.Instant;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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

@Entity
@Table(name = "invoices",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"provider_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(
        sql = "UPDATE invoices SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class StripeInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private StripeCustomer customer;

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
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}

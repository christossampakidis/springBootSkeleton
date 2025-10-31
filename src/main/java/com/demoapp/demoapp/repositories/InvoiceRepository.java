package com.demoapp.demoapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entities.StripeInvoice;

@Repository
public interface InvoiceRepository extends JpaRepository<StripeInvoice, Long> {
    Optional<StripeInvoice> findByProviderId(String providerId);
}

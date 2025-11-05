package com.demoapp.demoapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entity.StripeInvoice;

@Repository
public interface InvoiceRepository extends JpaRepository<StripeInvoice, Long> {
    Optional<StripeInvoice> findByProviderId(String providerId);

    void deleteByProviderId(String providerId);
}

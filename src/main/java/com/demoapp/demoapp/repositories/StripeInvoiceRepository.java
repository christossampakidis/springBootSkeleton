package com.demoapp.demoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entities.StripeInvoice;

@Repository
public interface StripeInvoiceRepository extends JpaRepository<StripeInvoice, Long> {
}

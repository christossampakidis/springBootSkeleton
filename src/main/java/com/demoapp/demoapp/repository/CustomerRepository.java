package com.demoapp.demoapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entity.StripeCustomer;

@Repository
public interface CustomerRepository
        extends JpaRepository<StripeCustomer, Long> {
    Optional<StripeCustomer> findByEmail(String email);

    Optional<StripeCustomer> findByProviderId(String providerId);

    void deleteByProviderId(String providerId);
}

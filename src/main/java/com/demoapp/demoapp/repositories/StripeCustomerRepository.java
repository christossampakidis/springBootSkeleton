package com.demoapp.demoapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.demoapp.entities.StripeCustomer;

@Repository
public interface StripeCustomerRepository extends JpaRepository<StripeCustomer, Long> {
    Optional<StripeCustomer> findByEmail(String email);
}

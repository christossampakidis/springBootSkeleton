package com.demoapp.demoapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.models.dto.CustomerDTO;
import com.demoapp.demoapp.repositories.StripeCustomerRepository;

@Service("customers")
public class CustomersService {

    @Autowired
    private StripeCustomerRepository stripeCustomerRepository;

    public List<CustomerDTO> fetchCustomers() {
        return stripeCustomerRepository.findAll().stream()
                .map(customer -> new CustomerDTO(
                        customer.getId(),
                        customer.getEmail(),
                        customer.getCreatedAt()))
                .toList();
    }
}

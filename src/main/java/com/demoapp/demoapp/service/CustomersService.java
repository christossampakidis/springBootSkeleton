package com.demoapp.demoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demoapp.demoapp.entity.StripeCustomer;
import com.demoapp.demoapp.model.dto.CustomerDTO;
import com.demoapp.demoapp.repository.CustomerRepository;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

@Service("customers")
public class CustomersService {

    private final CustomerRepository customerRepository;

    public CustomersService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Fetches all customers and maps them to CustomerDTOs.
     * 
     * @return List of CustomerDTOs
     */
    public List<CustomerDTO> fetchCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> new CustomerDTO(
                        customer.getId(),
                        customer.getEmail(),
                        customer.getCreatedAt()))
                .toList();
    }

    /**
     * Handles Stripe webhook events related to customers.
     * 
     * @param event
     */
    @Transactional
    public void handleEvent(Event event) {
        Optional<StripeObject> deserializer = event.getDataObjectDeserializer().getObject();
        if (deserializer.isPresent() && deserializer.get() instanceof Customer stripeCustomer) {
            switch (event.getType()) {
                case "customer.created" -> this.createCustomer(stripeCustomer);
                case "customer.deleted" -> this.deleteCustomer(stripeCustomer);
                default -> System.out.println("Unhandled event type: " + event.getType());
            }
        }
    }

    /**
     * Creates a Stripe customer with the given details.
     * 
     * @param stripeCustomer
     */
    public void createCustomer(Customer stripeCustomer) {
        StripeCustomer newCustomer = new StripeCustomer();
        newCustomer.setEmail(stripeCustomer.getEmail());
        newCustomer.setProviderId(stripeCustomer.getId());
        customerRepository.save(newCustomer);
    }

    /**
     * Deletes a Stripe customer by its provider ID.
     * 
     * @param stripeCustomer
     */
    public void deleteCustomer(Customer stripeCustomer) {
        customerRepository.deleteByProviderId(stripeCustomer.getId());
    }

}

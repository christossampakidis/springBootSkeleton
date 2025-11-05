package com.demoapp.demoapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demoapp.demoapp.entities.StripeCustomer;
import com.demoapp.demoapp.models.dto.CustomerDTO;
import com.demoapp.demoapp.repositories.CustomerRepository;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

@Service("customers")
public class CustomersService {

    @Autowired
    private CustomerRepository CustomerRepository;

    /**
     * Fetches all customers and maps them to CustomerDTOs.
     * @return List of CustomerDTOs
     */
    public List<CustomerDTO> fetchCustomers() {
        return CustomerRepository.findAll().stream()
                .map(customer -> new CustomerDTO(
                        customer.getId(),
                        customer.getEmail(),
                        customer.getCreatedAt()))
                .toList();
    }

    /**
     * Handles Stripe webhook events related to customers.
     * @param event
     */
    @Transactional
    public void handleEvent(Event event) {
        Optional<StripeObject> deserializer = event.getDataObjectDeserializer().getObject();
        if (deserializer.isPresent() && deserializer.get() instanceof Customer stripeCustomer) {
            switch (event.getType()) {
                case "customer.created":
                    this.createCustomer(stripeCustomer);
                    break;
                case "customer.deleted":
                    this.deleteCustomer(stripeCustomer);
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }
        }
    }

    /**
     * Creates a Stripe customer with the given details.
     * @param stripeCustomer
     */
    public void createCustomer(Customer stripeCustomer) {
        StripeCustomer newCustomer = new StripeCustomer();
        newCustomer.setEmail(stripeCustomer.getEmail());
        newCustomer.setProviderId(stripeCustomer.getId());
        CustomerRepository.save(newCustomer);
    }

    /**
     * Deletes a Stripe customer by its provider ID.
     * @param stripeCustomer
     */
    public void deleteCustomer(Customer stripeCustomer) {
        CustomerRepository.deleteByProviderId(stripeCustomer.getId());
    }

}

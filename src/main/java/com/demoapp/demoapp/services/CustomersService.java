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

    public List<CustomerDTO> fetchCustomers() {
        return CustomerRepository.findAll().stream()
                .map(customer -> new CustomerDTO(
                        customer.getId(),
                        customer.getEmail(),
                        customer.getCreatedAt()))
                .toList();
    }

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

    public void createCustomer(Customer stripeCustomer) {
        StripeCustomer newCustomer = new StripeCustomer();
        newCustomer.setEmail(stripeCustomer.getEmail());
        newCustomer.setProviderId(stripeCustomer.getId());
        CustomerRepository.save(newCustomer);
    }

    public void deleteCustomer(Customer stripeCustomer) {
        CustomerRepository.deleteByProviderId(stripeCustomer.getId());
    }

}

package com.demoapp.demoapp.service;

import java.util.List;
import java.util.Optional;

import com.demoapp.demoapp.service.interfaces.CustomersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demoapp.demoapp.entity.StripeCustomer;
import com.demoapp.demoapp.model.dto.CustomerDTO;
import com.demoapp.demoapp.repository.CustomerRepository;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

import lombok.RequiredArgsConstructor;

@Service("customers")
@RequiredArgsConstructor
public class CustomersServiceImpl implements CustomersService {

    private final CustomerRepository customerRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerDTO> fetchCustomers() {
        return customerRepository.findAll().stream()
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
                case "customer.created" -> this.createCustomer(stripeCustomer);
                case "customer.deleted" -> this.deleteCustomer(stripeCustomer);
                default -> System.out.println("Unhandled event type: " + event.getType());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCustomer(Customer stripeCustomer) {
        customerRepository.save(StripeCustomer.builder()
                .email(stripeCustomer.getEmail())
                .providerId(stripeCustomer.getId())
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCustomer(Customer stripeCustomer) {
        customerRepository.deleteByProviderId(stripeCustomer.getId());
    }

}

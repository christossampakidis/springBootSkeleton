package com.demoapp.demoapp.service.interfaces;

import java.util.List;

import com.demoapp.demoapp.model.dto.CustomerDTO;
import com.stripe.model.Customer;
import com.stripe.model.Event;

/**
 * Interface defining operations for managing Stripe customers.
 */
public interface CustomersService {

    /**
     * Fetches all customers and maps them to CustomerDTOs.
     *
     * @return {@link List} containing {@link CustomerDTO}
     */
    List<CustomerDTO> fetchCustomers();

    /**
     * Handles Stripe webhook events related to customers.
     *
     * @param event the Stripe {@link Event}
     */
    void handleEvent(Event event);

    /**
     * Creates a Stripe customer with the given details.
     *
     * @param stripeCustomer the Stripe {@link Customer}
     */
    void createCustomer(Customer stripeCustomer);

    /**
     * Deletes a Stripe customer by its provider ID.
     *
     * @param stripeCustomer the Stripe {@link Customer}
     */
    void deleteCustomer(Customer stripeCustomer);

}

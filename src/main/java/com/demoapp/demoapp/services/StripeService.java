package com.demoapp.demoapp.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entities.StripeCustomer;
import com.demoapp.demoapp.entities.StripeInvoice;
import com.demoapp.demoapp.entities.StripeItem;
import com.demoapp.demoapp.interfaces.PaymentProvider;
import com.demoapp.demoapp.models.InvoiceRequest;
import com.demoapp.demoapp.models.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.repositories.CustomerRepository;
import com.demoapp.demoapp.repositories.InvoiceRepository;
import com.demoapp.demoapp.repositories.ItemRepository;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.InvoiceSendInvoiceParams;

@Service("stripe")
public class StripeService implements PaymentProvider {

    @Value("${stripe.key.secret}")
    private String API_SECRET_KEY;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    ItemRepository itemRepository;

    public void processInvoice(InvoiceRequest invoiceRequest) throws Exception {
        Stripe.apiKey = API_SECRET_KEY;
        Customer stripeCustomer;
        Long userId = invoiceRequest.getUserId();

        StripeCustomer customer = customerRepository.findByUserId(userId).orElse(null);
        if (customer == null) {
            stripeCustomer = this.createCustomer(invoiceRequest.getEmail());
        } else {
            stripeCustomer = Customer.retrieve(customer.getProviderId());
        }

        Invoice stripeInvoice = createInvoice(stripeCustomer);
        for (ItemDTO item : invoiceRequest.getItems()) {
            this.createInvoiceItem(stripeCustomer, stripeInvoice, item);
        }
        InvoiceSendInvoiceParams invoiceSendParams = InvoiceSendInvoiceParams.builder().build();
        stripeInvoice.sendInvoice(invoiceSendParams);

    }

    public Customer createCustomer(String email) throws Exception {
        Random rand = new Random();
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(email)
                .setLimit(1L)
                .build();
        List<Customer> customers = Customer.list(params).getData();
        Customer customer;
        if (!customers.isEmpty()) {
            customer = customers.get(0);
        } else {
            CustomerCreateParams createParams = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setDescription("Customer for " + email)
                    .build();
            customer = Customer.create(createParams);
        }
        customerRepository.save(new StripeCustomer(rand.nextLong(), email, customer.getId()));
        return customer;
    }

    public Invoice createInvoice(Customer customer) throws Exception {
        String id = customer.getId();
        InvoiceCreateParams invoiceParams = InvoiceCreateParams
                .builder()
                .setCustomer(id)
                .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
                .setDaysUntilDue(30L)
                .build();
        Invoice stripeInvoice = Invoice.create(invoiceParams);
        StripeInvoice newInvoice = new StripeInvoice();
        newInvoice.setCustomer(customerRepository.findByProviderId(id).orElse(null));
        newInvoice.setProviderId(stripeInvoice.getId());
        invoiceRepository.save(newInvoice);
        return stripeInvoice;
    }

    public void createInvoiceItem(Customer customer, Invoice stripeInvoice, ItemDTO item) throws Exception {
        InvoiceItemCreateParams invoiceItemParams = InvoiceItemCreateParams.builder()
                .setCustomer(customer.getId())
                .setAmount(item.getAmount())
                .setInvoice(stripeInvoice.getId())
                .setCurrency("EUR")
                .setQuantity(item.getQuantity())
                .build();
        InvoiceItem stripeItem = InvoiceItem.create(invoiceItemParams);
        StripeItem newItem = new StripeItem();
        newItem.setInvoice(invoiceRepository.findByProviderId(stripeInvoice.getId()).orElse(null));
        newItem.setProviderId(stripeItem.getId());
        newItem.setAmount(item.getAmount());
        newItem.setQuantity(item.getQuantity());
        newItem.setCurrency("EUR");
        itemRepository.save(newItem);
    }
}

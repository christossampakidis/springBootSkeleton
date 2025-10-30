package com.demoapp.demoapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entities.Customer;
import com.demoapp.demoapp.entities.Invoice;
import com.demoapp.demoapp.entities.Item;
import com.demoapp.demoapp.interfaces.PaymentProvider;
import com.demoapp.demoapp.models.InvoiceRequest;
import com.demoapp.demoapp.models.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.repositories.CustomerRepository;
import com.demoapp.demoapp.repositories.InvoiceRepository;
import com.demoapp.demoapp.repositories.ItemRepository;
import com.stripe.Stripe;
import com.stripe.model.CustomerCollection;
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
    CustomerRepository CustomerRepository;
    @Autowired
    InvoiceRepository InvoiceRepository;
    @Autowired
    ItemRepository ItemRepository;

    public void createInvoice(InvoiceRequest invoiceRequest) throws Exception {
        Stripe.apiKey = API_SECRET_KEY;
        com.stripe.model.Customer stripeCustomer;
        String email = invoiceRequest.getEmail();
        Customer customer = CustomerRepository.findByEmail(email).orElse(null);
        String customerId = null;
        if (customer == null) {
            stripeCustomer = findOrCreateCustomer(email);
            customer = CustomerRepository.save(new Customer(email, stripeCustomer.getId()));
            customerId = stripeCustomer.getId();
        } else {
            customerId = customer.getProviderId();
        }
        InvoiceCreateParams invoiceParams = InvoiceCreateParams
                .builder()
                .setCustomer(customerId)
                .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
                .setDaysUntilDue(30L)
                .build();
        com.stripe.model.Invoice stripeInvoice = com.stripe.model.Invoice.create(invoiceParams);
        Invoice newInvoice = new Invoice();
        newInvoice.setCustomer(customer);
        newInvoice.setProviderId(stripeInvoice.getId());
        InvoiceRepository.save(newInvoice);
        for (ItemDTO item : invoiceRequest.getItems()) {
            InvoiceItemCreateParams invoiceItemParams = InvoiceItemCreateParams.builder()
                    .setCustomer(customerId)
                    .setAmount(item.getAmount())
                    .setInvoice(stripeInvoice.getId())
                    .setCurrency("EUR")
                    .setQuantity(item.getQuantity())
                    .build();
            InvoiceItem stripeItem = InvoiceItem.create(invoiceItemParams);
            Item newItem = new Item();
            newItem.setInvoice(newInvoice);
            newItem.setProviderId(stripeItem.getId());
            newItem.setAmount(item.getAmount());
            newItem.setQuantity(item.getQuantity());
            newItem.setCurrency("EUR");
            ItemRepository.save(newItem);
        }
        InvoiceSendInvoiceParams invoiceSendParams = InvoiceSendInvoiceParams.builder().build();
        stripeInvoice.sendInvoice(invoiceSendParams);

    }

    public com.stripe.model.Customer findOrCreateCustomer(String email) throws Exception {
        CustomerListParams listParams = CustomerListParams.builder()
                .setEmail(email)
                .setLimit(1L)
                .build();

        CustomerCollection customers = com.stripe.model.Customer.list(listParams);

        if (!customers.getData().isEmpty()) {
            return customers.getData().get(0);
        }

        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setEmail(email)
                .setDescription("Customer for " + email)
                .build();

        return com.stripe.model.Customer.create(createParams);
    }
}

package com.demoapp.demoapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entities.StripeCustomer;
import com.demoapp.demoapp.entities.StripeInvoice;
import com.demoapp.demoapp.entities.StripeItem;
import com.demoapp.demoapp.models.InvoiceRequest;
import com.demoapp.demoapp.models.InvoiceRequest.Item;
import com.demoapp.demoapp.repositories.StripeCustomerRepository;
import com.demoapp.demoapp.repositories.StripeInvoiceRepository;
import com.demoapp.demoapp.repositories.StripeItemRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.InvoiceSendInvoiceParams;

@Service
public class StripeService {

    @Autowired
    StripeCustomerRepository stripeCustomerRepository;
    @Autowired
    StripeInvoiceRepository stripeInvoiceRepository;
    @Autowired
    StripeItemRepository stripeItemRepository;

    public void createInvoice(InvoiceRequest invoiceRequest) throws StripeException {
        Stripe.apiKey = "sk_test_51RdXw0Q05XjO94ClZ1PWcawabm8TdjiYPODemVl8jBq0jdvummCQNpcCyZCblkEct9XbqDCwbuzARy0uARnC2LvE00SL1AHEOz";
        Customer stripeCustomer;
        String email = invoiceRequest.getEmail();
        StripeCustomer customer = stripeCustomerRepository.findByEmail(email).orElse(null);
        String customerId = null;
        Long customerEntityId = null;
        if (customer == null) {
            CustomerCreateParams params = CustomerCreateParams
                    .builder()
                    .setEmail(email)
                    .setDescription(email)
                    .build();
            stripeCustomer = Customer.create(params);
            customerId = stripeCustomer.getId();
            StripeCustomer newCustomer = new StripeCustomer();
            newCustomer.setEmail(email);
            newCustomer.setStripeId(customerId);
            customerEntityId = newCustomer.getId();
            stripeCustomerRepository.save(newCustomer);
        } else {
            customerId = customer.getStripeId();
            customerEntityId = customer.getId();
        }
        InvoiceCreateParams invoiceParams = InvoiceCreateParams
                .builder()
                .setCustomer(customerId)
                .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
                .setDaysUntilDue(30L)
                .build();
        Invoice invoice = Invoice.create(invoiceParams);
        StripeInvoice newInvoice = new StripeInvoice();
        newInvoice.setCustomerId(customerEntityId);
        newInvoice.setDaysExpire(30L);
        newInvoice.setStripeId(invoice.getId());
        newInvoice.setInvoiceNumber(invoice.getNumber());
        stripeInvoiceRepository.save(newInvoice);
        Long invoiceId = newInvoice.getId();
        for (Item item : invoiceRequest.getItems()) {
            InvoiceItemCreateParams invoiceItemParams = InvoiceItemCreateParams.builder()
                    .setCustomer(customerId)
                    .setAmount(item.getAmount() * 100)
                    .setInvoice(invoice.getId())
                    .setCurrency("EUR")
                    .build();
            InvoiceItem stripeItem = InvoiceItem.create(invoiceItemParams);
            StripeItem newItem = new StripeItem();
            newItem.setCustomerId(customerEntityId);
            newItem.setInvoiceId(invoiceId);
            newItem.setStripeId(stripeItem.getId());
            newItem.setAmount(item.getAmount() * 100);
            newItem.setCurrency("EUR");
            stripeItemRepository.save(newItem);
        }
        InvoiceSendInvoiceParams invoiceSendParams = InvoiceSendInvoiceParams.builder().build();
        invoice.sendInvoice(invoiceSendParams);

    }
}

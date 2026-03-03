package com.demoapp.demoapp.integration.stripe;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demoapp.demoapp.entity.StripeCustomer;
import com.demoapp.demoapp.entity.StripeInvoice;
import com.demoapp.demoapp.entity.StripeItem;
import com.demoapp.demoapp.model.request.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.model.request.PaymentIntentRequest;
import com.demoapp.demoapp.repository.CustomerRepository;
import com.demoapp.demoapp.repository.InvoiceRepository;
import com.demoapp.demoapp.repository.ItemRepository;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.InvoiceSendInvoiceParams;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StripeClientImpl implements StripeClient {

    @Value("${stripe.key.secret}")
    private String API_SECRET_KEY;

    private final CustomerRepository customerRepository;

    private final InvoiceRepository invoiceRepository;

    private final ItemRepository itemRepository;

    private void init() {
        Stripe.apiKey = API_SECRET_KEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer createCustomer(String email) throws Exception {
        init();
        StripeCustomer localCustomer = customerRepository.findByEmail(email).orElse(null);
        if (localCustomer == null) {
            CustomerListParams params = CustomerListParams.builder()
                    .setEmail(email)
                    .setLimit(1L)
                    .build();
            List<Customer> customers = Customer.list(params).getData();
            Customer customer;
            if (!customers.isEmpty()) {
                customer = customers.getFirst();
            } else {
                CustomerCreateParams createParams = CustomerCreateParams.builder()
                        .setEmail(email)
                        .setDescription("Customer for " + email)
                        .build();
                customer = Customer.create(createParams);
            }
            customerRepository.save(StripeCustomer.builder()
                    .email(email)
                    .providerId(customer.getId())
                    .build());
            return customer;
        } else {
            return Customer.retrieve(localCustomer.getProviderId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer retrieveCustomer(String providerId) throws Exception {
        init();
        return Customer.retrieve(providerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice createInvoice(Customer customer) throws Exception {
        init();
        String id = customer.getId();
        InvoiceCreateParams invoiceParams = InvoiceCreateParams.builder()
                .setCustomer(id)
                .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
                .setDaysUntilDue(30L)
                .build();
        Invoice stripeInvoice = Invoice.create(invoiceParams);
        invoiceRepository.save(StripeInvoice.builder()
                .customer(customerRepository.findByProviderId(id).orElse(null))
                .providerId(stripeInvoice.getId())
                .build());
        return stripeInvoice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendInvoice(Invoice invoice) throws Exception {
        InvoiceSendInvoiceParams invoiceSendParams = InvoiceSendInvoiceParams.builder().build();
        invoice.sendInvoice(invoiceSendParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createInvoiceItem(Customer customer, Invoice invoice, ItemDTO item) throws Exception {
        init();
        InvoiceItemCreateParams invoiceItemParams = InvoiceItemCreateParams.builder()
                .setDescription(item.description())
                .setCustomer(customer.getId())
                .setUnitAmountDecimal(item.unitAmount())
                .setInvoice(invoice.getId())
                .setCurrency("EUR")
                .setQuantity(item.quantity())
                .build();
        InvoiceItem stripeItem = InvoiceItem.create(invoiceItemParams);

        itemRepository.save(StripeItem.builder()
                .invoice(invoiceRepository.findByProviderId(invoice.getId()).orElse(null))
                .providerId(stripeItem.getId())
                .description(item.description())
                .unitAmount(item.unitAmount())
                .quantity(item.quantity())
                .currency("EUR")
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void voidInvoice(String invoiceId) throws Exception {
        init();
        Invoice stripeInvoice = Invoice.retrieve(invoiceId);
        stripeInvoice.voidInvoice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentIntent createPaymentIntent(PaymentIntentRequest request) throws Exception {
        init();
        Customer stripeCustomer;
        String email = request.email();

        StripeCustomer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer == null) {
            stripeCustomer = createCustomer(email);
        } else {
            stripeCustomer = retrieveCustomer(customer.getProviderId());
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.amount())
                .setCurrency("eur")
                .setCustomer(stripeCustomer.getId())
                .addPaymentMethodType("card")
                .addPaymentMethodType("paypal")
                .build();

        return PaymentIntent.create(params);
    }
}

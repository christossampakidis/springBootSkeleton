package com.demoapp.demoapp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demoapp.demoapp.entities.StripeCustomer;
import com.demoapp.demoapp.entities.StripeInvoice;
import com.demoapp.demoapp.entities.StripeItem;
import com.demoapp.demoapp.interfaces.PaymentProvider;
import com.demoapp.demoapp.models.requests.InvoiceRequest;
import com.demoapp.demoapp.models.requests.InvoiceRequest.ItemDTO;
import com.demoapp.demoapp.models.requests.PaymentIntentRequest;
import com.demoapp.demoapp.repositories.CustomerRepository;
import com.demoapp.demoapp.repositories.InvoiceRepository;
import com.demoapp.demoapp.repositories.ItemRepository;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSession;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.CustomerSessionCreateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.InvoiceSendInvoiceParams;
import com.stripe.param.PaymentIntentCreateParams;

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

    /**
     * Processes an invoice request by creating a customer (if not existing), creating an invoice,
     * adding invoice items, and sending the invoice.
     * @param invoiceRequest
     * @throws Exception
     */
    public void processInvoice(InvoiceRequest invoiceRequest) throws Exception {
        Stripe.apiKey = API_SECRET_KEY;
        Customer stripeCustomer;
        String email = invoiceRequest.getEmail();

        StripeCustomer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer == null) {
            stripeCustomer = this.createCustomer(email);
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

    /**
     * Creates a Stripe customer with the given email if not already existing.
     * @param email
     * @return
     * @throws Exception
     */
    public Customer createCustomer(String email) throws Exception {
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
        customerRepository.save(new StripeCustomer(email, customer.getId()));
        return customer;
    }

    /**
     * Voids an invoice by its ID.
     * @param invoiceId
     * @throws Exception
     */
    public void voidInvoice(Long invoiceId) throws Exception {
        Stripe.apiKey = API_SECRET_KEY;
        Optional<StripeInvoice> invoice = invoiceRepository.findById(invoiceId);
        Invoice stripeInvoice = Invoice.retrieve(invoice.get().getProviderId());
        stripeInvoice.voidInvoice();
    }

    /**
     * Creates an invoice for the given customer.
     * @param customer
     * @return
     * @throws Exception
     */
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

    /**
     * Creates an invoice item for the given customer and invoice.
     * @param customer
     * @param stripeInvoice
     * @param item
     * @throws Exception
     */
    public void createInvoiceItem(Customer customer, Invoice stripeInvoice, ItemDTO item) throws Exception {
        InvoiceItemCreateParams invoiceItemParams = InvoiceItemCreateParams.builder()
                .setDescription(item.getDescription())
                .setCustomer(customer.getId())
                .setUnitAmountDecimal(item.getUnitAmount())
                .setInvoice(stripeInvoice.getId())
                .setCurrency("EUR")
                .setQuantity(item.getQuantity())
                .build();
        InvoiceItem stripeItem = InvoiceItem.create(invoiceItemParams);
        StripeItem newItem = new StripeItem();
        newItem.setInvoice(invoiceRepository.findByProviderId(stripeInvoice.getId()).orElse(null));
        newItem.setProviderId(stripeItem.getId());
        newItem.setDescription(item.getDescription());
        newItem.setUnitAmount(item.getUnitAmount());
        newItem.setQuantity(item.getQuantity());
        newItem.setCurrency("EUR");
        itemRepository.save(newItem);
    }

    /**
     * Creates a payment intent for the given payment intent request.
     * @param paymentIntentRequest
     * @return
     * @throws Exception
     */
    public Map<String, String> createPaymentIntent(PaymentIntentRequest paymentIntentRequest) throws Exception {
        Stripe.apiKey = API_SECRET_KEY;
        Customer stripeCustomer;
        String email = paymentIntentRequest.getEmail();

        StripeCustomer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer == null) {
            stripeCustomer = this.createCustomer(email);
        } else {
            stripeCustomer = Customer.retrieve(customer.getProviderId());
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentIntentRequest.getAmount())
                .setCurrency("eur")
                .setCustomer(stripeCustomer.getId())
                .addPaymentMethodType("card")
                .addPaymentMethodType("paypal")
                .build();

        Map<String, String> map = new HashMap<>();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            CustomerSessionCreateParams csParams = CustomerSessionCreateParams.builder()
                    .setCustomer(stripeCustomer.getId())
                    .setComponents(CustomerSessionCreateParams.Components.builder().build())
                    .putExtraParam("components[payment_element][enabled]", true)
                    .putExtraParam(
                            "components[payment_element][features][payment_method_redisplay]",
                            "enabled")
                    .putExtraParam(
                            "components[payment_element][features][payment_method_save]",
                            "enabled")
                    .putExtraParam(
                            "components[payment_element][features][payment_method_save_usage]",
                            "off_session")
                    .putExtraParam(
                            "components[payment_element][features][payment_method_remove]",
                            "enabled")
                    .build();
            CustomerSession customerSession = CustomerSession.create(csParams);
            map.put("client_secret", paymentIntent.getClientSecret());
            map.put("customerSessionClientSecret", customerSession.getClientSecret());
        } catch (CardException e) {
            map.put("error", e.getUserMessage());
        } catch (Exception e) {
            map.put("error", e.getMessage());
        }
        return map;
    }

}

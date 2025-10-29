package com.demoapp.demoapp.factories;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.demoapp.demoapp.interfaces.PaymentProvider;

@Component
public class PaymentFactory {

    private final Map<String, PaymentProvider> providers;

    public PaymentFactory(Map<String, PaymentProvider> providers) {
        this.providers = providers;
    }

    public PaymentProvider getProvider(String providerName) {
        PaymentProvider provider = providers.get(providerName.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("No provider found with name: " + providerName);
        }
        return provider;
    }
}
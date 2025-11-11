package com.demoapp.demoapp.configuration;

import java.util.Map;

import com.demoapp.demoapp.context.TenantContext;
import lombok.Setter;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Setter
public class TenantIdentifierResolver
        implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    private String currentTenant = "default";

    @Override
    public String resolveCurrentTenantIdentifier() {

        if (TenantContext.get() != null && StringUtils.hasText(TenantContext.get().getTenantId())) {
            currentTenant = TenantContext.get().getTenantId();
        }

        return currentTenant;

    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
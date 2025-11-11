package com.demoapp.demoapp.filter;

import java.io.IOException;

import com.demoapp.demoapp.context.TenantContext;
import org.springframework.stereotype.Component;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantId = httpRequest.getHeader("x-tenant-id");

        TenantContext.set(TenantContext.builder().tenantId(tenantId).build());
        chain.doFilter(request, response);
        TenantContext.clear();
    }
}
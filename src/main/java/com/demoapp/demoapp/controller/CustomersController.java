package com.demoapp.demoapp.controller;

import java.util.Map;

import com.demoapp.demoapp.service.interfaces.CustomersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomersController {

    private final CustomersService customersService;

    public CustomersController(CustomersService customersService){
        this.customersService = customersService;
    }

    /**
     * Fetches all customers.
     * 
     * @return ResponseEntity with customer data or error message.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getCustomers() {
            return ResponseEntity.ok(Map.of("message", customersService.fetchCustomers()));
    }
}

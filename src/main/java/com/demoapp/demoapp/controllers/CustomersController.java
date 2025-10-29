package com.demoapp.demoapp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.demoapp.services.CustomersService;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomersController {

    @Autowired
    CustomersService customersService;

    @GetMapping
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getCustomers() {
        try {
            return ResponseEntity.ok(Map.of("message", customersService.fetchCustomers()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error fetching customers"));
        }
    }
}

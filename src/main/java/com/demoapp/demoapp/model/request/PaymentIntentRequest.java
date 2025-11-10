package com.demoapp.demoapp.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentIntentRequest {
    @NotBlank @Email
    private final String email;
    @NotBlank
    private final Long amount;
}

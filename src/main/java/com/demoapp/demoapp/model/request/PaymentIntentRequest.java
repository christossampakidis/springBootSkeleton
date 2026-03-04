package com.demoapp.demoapp.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PaymentIntentRequest(@NotBlank @Email String email,
        @NotBlank Long amount) {
}

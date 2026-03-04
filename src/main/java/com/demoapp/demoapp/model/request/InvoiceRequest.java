package com.demoapp.demoapp.model.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record InvoiceRequest(@NotBlank @Email String email,
                @NotEmpty List<ItemDTO> items) {
        public record ItemDTO(@NotBlank String description,
                        @NotBlank BigDecimal unitAmount,
                        @NotBlank Long quantity) {
        }
}

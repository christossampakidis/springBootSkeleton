package com.demoapp.demoapp.model.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
public class InvoiceRequest {
    @NotBlank
    @Email
    private final String email;
    @NotEmpty
    private final List<ItemDTO> items;

    @Data
    public static class ItemDTO {
        @NotBlank
        private final String description;
        @NotBlank
        private final BigDecimal unitAmount;
        @NotBlank
        private final Long quantity;

    }
}

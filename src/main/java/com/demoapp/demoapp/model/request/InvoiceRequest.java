package com.demoapp.demoapp.model.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceRequest {
    @NotBlank
    @Email
    private String email;
    @NotEmpty
    private List<ItemDTO> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDTO {
        @NotBlank
        private String description;
        @NotBlank
        private BigDecimal unitAmount;
        @NotBlank
        private Long quantity;

    }
}

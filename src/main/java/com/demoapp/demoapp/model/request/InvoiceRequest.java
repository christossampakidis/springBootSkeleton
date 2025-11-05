package com.demoapp.demoapp.model.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceRequest {
    private String email;
    private List<ItemDTO> items;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemDTO {
        private String description;
        private BigDecimal unitAmount;
        private Long quantity;

        public ItemDTO(String description, BigDecimal unitAmount, Long quantity) {
            this.description = description;
            this.unitAmount = unitAmount;
            this.quantity = quantity;
        }

    }
}

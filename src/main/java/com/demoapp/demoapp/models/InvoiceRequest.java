package com.demoapp.demoapp.models;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceRequest {

    private Long userId;
    private String email;
    private List<ItemDTO> items;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemDTO {
        private String name;
        private String description;
        private Long amount;
        private Long quantity;

        public ItemDTO(String name, String description, Long amount, Long quantity) {
            this.name = name;
            this.description = description;
            this.amount = amount;
            this.quantity = quantity;
        }

    }
}

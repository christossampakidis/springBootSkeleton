package com.demoapp.demoapp.models;

import java.util.List;

public class InvoiceRequest {

    private String email;
    private List<Item> items;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String name;
        private String description;
        private Long amount;

        public Item() {
        }

        public Item(String name, String description, Long amount) {
            this.name = name;
            this.description = description;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }
    }
}

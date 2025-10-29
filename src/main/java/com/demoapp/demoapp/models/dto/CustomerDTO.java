package com.demoapp.demoapp.models.dto;

import java.util.Date;

public class CustomerDTO {
    private Long id;
    private String email;
    private Date createdAt;

    public CustomerDTO(Long id, String email, Date createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

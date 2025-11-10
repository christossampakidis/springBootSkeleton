package com.demoapp.demoapp.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerDTO {
    private final Long id;
    private final String email;
    private final  Date createdAt;
}

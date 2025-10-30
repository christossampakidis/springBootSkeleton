package com.demoapp.demoapp.models.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private String email;
    private Date createdAt;

    public CustomerDTO(Long id, String email, Date createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

}

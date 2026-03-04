package com.demoapp.demoapp.model.dto;

import java.util.Date;

public record CustomerDTO(Long id, String email, Date createdAt) {
}

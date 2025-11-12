package com.demoapp.demoapp.model.dto;

public record FileDTO(
        byte[] content,
        String contentType,
        String fileName
) { }

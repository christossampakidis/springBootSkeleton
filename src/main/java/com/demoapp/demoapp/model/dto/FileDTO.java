package com.demoapp.demoapp.model.dto;

import lombok.Data;

@Data
public class FileDTO {
    private final byte[] content;
    private final String contentType;
    private final String fileName;
}

package com.demoapp.demoapp.model.dto;

import lombok.Data;

import java.time.Instant;


@Data
public class S3FileDTO {
    private final String name;
    private final Long size;
    private final Instant lastModified;
}

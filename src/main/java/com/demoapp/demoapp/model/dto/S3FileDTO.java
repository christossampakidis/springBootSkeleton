package com.demoapp.demoapp.model.dto;

import java.time.Instant;


public record S3FileDTO(
        String name,
        Long size,
        Instant lastModified
) { }

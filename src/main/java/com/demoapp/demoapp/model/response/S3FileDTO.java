package com.demoapp.demoapp.model.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class S3FileDTO {
    private String name;
    private Long size;
    private Instant lastModified;
}

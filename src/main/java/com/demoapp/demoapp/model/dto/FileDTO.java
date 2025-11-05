package com.demoapp.demoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDTO {
    private byte[] content;
    private String contentType;
    private String fileName;

    public FileDTO(byte[] content, String contentType, String fileName) {
        this.content = content;
        this.contentType = contentType;
        this.fileName = fileName;
    }
}

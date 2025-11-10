package com.demoapp.demoapp.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO<T> {
    private final List<T> content;
    private final int pageNumber;
    private final  int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;
}
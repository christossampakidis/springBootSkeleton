package com.demoapp.demoapp.model.dto;

import java.util.List;

public record PageDTO<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) { }
package com.iso.spring3.core.models;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageModel<T> {
    public PageModel(Page<T> page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.data = page.getContent();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
}

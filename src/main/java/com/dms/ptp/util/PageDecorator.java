package com.dms.ptp.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageDecorator<T> {

    private final Page<T> page;

    public PageDecorator(Page<T> page) {
        this.page = page;
    }

    @JsonProperty("items")
    public List<T> getContent() {
        return this.page.getContent();
    }

    @JsonProperty("total")
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @JsonProperty("pages")
    public int getTotalPages() {
        return page.getTotalPages();
    }

}
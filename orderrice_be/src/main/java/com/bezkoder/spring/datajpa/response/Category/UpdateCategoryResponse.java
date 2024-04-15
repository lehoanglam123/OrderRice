package com.bezkoder.spring.datajpa.response.Category;

import com.bezkoder.spring.datajpa.model.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCategoryResponse {
    private String status;
    private String message;

    private Category data;
}

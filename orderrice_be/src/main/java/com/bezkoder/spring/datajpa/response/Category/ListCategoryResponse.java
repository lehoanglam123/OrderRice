package com.bezkoder.spring.datajpa.response.Category;

import com.bezkoder.spring.datajpa.model.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListCategoryResponse {
    private String status;
    private String message;

    private List<Category> listdata;
}

package com.bezkoder.spring.datajpa.response.Category;

import java.util.List;
import com.bezkoder.spring.datajpa.model.CategoryLimit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListCategoryByLimit {
    private String status;
    private String message;

    private List<CategoryLimit> listdata;
}

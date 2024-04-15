package com.bezkoder.spring.datajpa.request;

import java.util.List;
import com.bezkoder.spring.datajpa.model.Category;
import lombok.Data;

@Data
public class ListCategoryRequest {
	private List<Category> listCategoryRequest;
}
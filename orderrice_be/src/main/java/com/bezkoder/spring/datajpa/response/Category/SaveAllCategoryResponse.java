package com.bezkoder.spring.datajpa.response.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveAllCategoryResponse {
	private String status;
	private String message;
}

package com.bezkoder.spring.datajpa.request;

import java.util.List;

public class ListCategory {
	
	List<Integer> categoryId;

	public List<Integer> getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(List<Integer> categoryId) {
		this.categoryId = categoryId;
	}

}

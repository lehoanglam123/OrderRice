package com.bezkoder.spring.datajpa.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bezkoder.spring.datajpa.model.Category;
import com.bezkoder.spring.datajpa.model.CategoryLimit;
import com.bezkoder.spring.datajpa.request.ListCategory;
import com.bezkoder.spring.datajpa.request.ListCategoryRequest;

@Mapper
public interface CategoryRepository {
	public List<Category> getAllCategory();

	public boolean createCategory(Category category);

	public Category getCategory(String foodName);

	public Category getCategoryById(int id);
	
	public boolean updateCategoryById(Category category);
	
	public Category getCategoryNotInId(String foodName, int id);
	
	
	public List<Category> selectFor(ListCategory listId);
	
	public boolean deleteFor(ListCategory listId);

	public List<Category> selectAllCategory();

	public boolean deleteAllCategory();

	public boolean updateAllCategory();

	public  List<CategoryLimit> getCategoryLimitByDate(); 
	
	public List<Category> SelectCategoryByFoodName(ListCategoryRequest listCategoryRequest);

	public Boolean saveCategoryByFoodName(ListCategoryRequest listCategoryRequest); 
	
}

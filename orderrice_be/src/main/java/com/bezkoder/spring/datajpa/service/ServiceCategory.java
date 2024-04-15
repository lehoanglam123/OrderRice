package com.bezkoder.spring.datajpa.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.datajpa.model.Category;
import com.bezkoder.spring.datajpa.request.ListCategory;
import com.bezkoder.spring.datajpa.request.ListCategoryRequest;
import com.bezkoder.spring.datajpa.response.Category.CreateCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.DeleteCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryByLimit;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.SaveAllCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.UpdateCategoryResponse;

@Service
public interface ServiceCategory {

	public ResponseEntity<ListCategoryResponse> getAll();
	public ResponseEntity<CreateCategoryResponse> createCategory(Category category);
	public ResponseEntity<UpdateCategoryResponse> updateCategory(int id, Category category);
	public ResponseEntity<DeleteCategoryResponse> deleteCategoryById(ListCategory lstCategoryId);
	public ResponseEntity<ListCategoryResponse> selectAllCategory();
	public ResponseEntity<DeleteCategoryResponse> deleteAllCategory();
	public ResponseEntity<UpdateCategoryResponse> updateAllCategory();
	public ResponseEntity<SaveAllCategoryResponse> saveAll(ListCategoryRequest listCategoryRequest);
	public ResponseEntity<ListCategoryByLimit> getCategoryLimitByDate();
}

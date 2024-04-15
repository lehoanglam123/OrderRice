package com.bezkoder.spring.datajpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.model.Category;
import com.bezkoder.spring.datajpa.request.ListCategory;
import com.bezkoder.spring.datajpa.request.ListCategoryRequest;
import com.bezkoder.spring.datajpa.response.Category.CreateCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.DeleteCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryByLimit;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.SaveAllCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.UpdateCategoryResponse;
import com.bezkoder.spring.datajpa.service.ServiceCategory;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/category")
@ResponseBody
public class CategoryController {
	@Autowired
	private ServiceCategory serviceCategory;

	@GetMapping("/getall")
	public ResponseEntity<ListCategoryResponse> getAllCategory()
	{
		return serviceCategory.getAll();
	}

	@PostMapping("/create")
	public ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody Category category) {
		return serviceCategory.createCategory(category);
	}

	@PostMapping("update/{id}")
	public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable(required = true) int id,
																 @RequestBody Category category) {
		return serviceCategory.updateCategory(id, category);
	}
	
	@PostMapping("/saveAll")
	public ResponseEntity<SaveAllCategoryResponse> insertListCategory(@RequestBody ListCategoryRequest listCategoryRequest) {
		return serviceCategory.saveAll(listCategoryRequest);
	}

	@DeleteMapping("delete")
	public ResponseEntity<DeleteCategoryResponse> deleteCategory(@RequestBody(required = true) ListCategory lstCategoryId) {
		return serviceCategory.deleteCategoryById(lstCategoryId);
	}

	@GetMapping("/getactivity")
	public ResponseEntity<ListCategoryResponse> deleteAllCategory(){
		return serviceCategory.selectAllCategory();
	}

	@DeleteMapping("/deleteAll")
	public ResponseEntity<DeleteCategoryResponse> delete(){
		return serviceCategory.deleteAllCategory();
	}

	@PostMapping("/updateAll")
	public ResponseEntity<UpdateCategoryResponse> update()
	{
		return serviceCategory.updateAllCategory();
	}
	
	@GetMapping("/getCategoryLimit")
	public ResponseEntity<ListCategoryByLimit> getCategoryLimitByDate(){
		return serviceCategory.getCategoryLimitByDate();
	}
}

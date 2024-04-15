package com.bezkoder.spring.datajpa.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.datajpa.model.Category;
import com.bezkoder.spring.datajpa.model.CategoryLimit;
import com.bezkoder.spring.datajpa.repository.CategoryRepository;
import com.bezkoder.spring.datajpa.request.ListCategory;
import com.bezkoder.spring.datajpa.request.ListCategoryRequest;
import com.bezkoder.spring.datajpa.response.Category.CreateCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.DeleteCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryByLimit;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.SaveAllCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.UpdateCategoryResponse;
import com.bezkoder.spring.datajpa.service.ServiceCategory;

@Service
public class ServiceCategoryimpl implements ServiceCategory {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ResponseEntity<ListCategoryResponse> getAll() {
		List<Category> category = categoryRepository.getAllCategory();
		ListCategoryResponse response = ListCategoryResponse.builder().listdata(category).status("200")
				.message("Successfully!").build();
		return new ResponseEntity<ListCategoryResponse>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UpdateCategoryResponse> updateCategory(int id, Category category) {
		Category categoryById = categoryRepository.getCategoryById(id);
		if (categoryById != null) {
			Category categoryByFoodName = categoryRepository.getCategoryNotInId(category.getFoodName(), id);
			if (categoryByFoodName != null) {
				UpdateCategoryResponse response = UpdateCategoryResponse.builder().status("400")
						.message("Food Name is Exists!").build();
				return new ResponseEntity<UpdateCategoryResponse>(response, HttpStatus.NO_CONTENT);
			}
			categoryById.setFoodName(category.getFoodName());
			categoryById.setPrice(category.getPrice());
			categoryById.setActive(category.getActive());
			categoryRepository.updateCategoryById(categoryById);
			UpdateCategoryResponse response = UpdateCategoryResponse.builder().data(categoryById).status("200")
					.message("Update successfully!").build();
			return new ResponseEntity<UpdateCategoryResponse>(response, HttpStatus.OK);
		} else {
			UpdateCategoryResponse response = UpdateCategoryResponse.builder().status("204")
					.message("Not found food by id!").build();
			return new ResponseEntity<UpdateCategoryResponse>(response, HttpStatus.NO_CONTENT);
		}
	}

	@Override
	public ResponseEntity<DeleteCategoryResponse> deleteCategoryById(ListCategory lstCategoryId) {
		int count = lstCategoryId.getCategoryId().size();
		List<Category> cate = categoryRepository.selectFor(lstCategoryId);
		if (cate.size() == count) {
			categoryRepository.deleteFor(lstCategoryId);
			DeleteCategoryResponse response = DeleteCategoryResponse.builder().status("200")
					.message("Delete Successfully!").build();
			return new ResponseEntity<DeleteCategoryResponse>(response, HttpStatus.OK);
		}
		DeleteCategoryResponse response = DeleteCategoryResponse.builder().status("204").message("No data!").build();
		return new ResponseEntity<DeleteCategoryResponse>(response, HttpStatus.NOT_IMPLEMENTED);
	}

	@Override
	public ResponseEntity<ListCategoryResponse> selectAllCategory() {
		List<Category> categories = categoryRepository.selectAllCategory();
		ListCategoryResponse deleteCategoryResponse = ListCategoryResponse.builder().status("200").listdata(categories)
				.message("Successfully!").build();
		return new ResponseEntity<ListCategoryResponse>(deleteCategoryResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DeleteCategoryResponse> deleteAllCategory() {
		categoryRepository.deleteAllCategory();
		DeleteCategoryResponse deleteCategoryResponse = DeleteCategoryResponse.builder().status("200")
				.message("Delete Successful").build();
		return new ResponseEntity<DeleteCategoryResponse>(deleteCategoryResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UpdateCategoryResponse> updateAllCategory() {
		categoryRepository.updateAllCategory();
		UpdateCategoryResponse updateCategoryResponse = UpdateCategoryResponse.builder().status("200")
				.message("Update Successful").build();
		return new ResponseEntity<UpdateCategoryResponse>(updateCategoryResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CreateCategoryResponse> createCategory(Category category) {
		Category cate = categoryRepository.getCategory(category.getFoodName());
		if (cate == null) {
			categoryRepository.createCategory(category);
			CreateCategoryResponse response = CreateCategoryResponse.builder().status("200")
					.message("Created Successfully!").build();
			return new ResponseEntity<CreateCategoryResponse>(response, HttpStatus.CREATED);
		}
		CreateCategoryResponse response = CreateCategoryResponse.builder().status("302").message("FoodName is existed!")
				.build();
		return new ResponseEntity<CreateCategoryResponse>(response, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<SaveAllCategoryResponse> saveAll(ListCategoryRequest listCategoryRequest) {
		List<Category> list = categoryRepository.SelectCategoryByFoodName(listCategoryRequest);
		if (list.isEmpty()) {
			categoryRepository.saveCategoryByFoodName(listCategoryRequest);
			SaveAllCategoryResponse respone = SaveAllCategoryResponse.builder().status("200")
					.message("Save successfully").build();
			return new ResponseEntity<SaveAllCategoryResponse>(respone, HttpStatus.OK);
		}
		SaveAllCategoryResponse respone = SaveAllCategoryResponse.builder().status("404").message("Save failed")
				.build();
		return new ResponseEntity<SaveAllCategoryResponse>(respone, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<ListCategoryByLimit> getCategoryLimitByDate() {
		List<CategoryLimit> categoryList = categoryRepository.getCategoryLimitByDate();
		if (categoryList.isEmpty()) {
			ListCategoryByLimit response = ListCategoryByLimit.builder().status("204").message("Get Not Content")
					.build();
			return new ResponseEntity<ListCategoryByLimit>(response, HttpStatus.NO_CONTENT);
		}
		ListCategoryByLimit response = ListCategoryByLimit.builder().status("200").message("Data")
				.listdata(categoryList).build();
		return new ResponseEntity<ListCategoryByLimit>(response, HttpStatus.OK);
	}
}
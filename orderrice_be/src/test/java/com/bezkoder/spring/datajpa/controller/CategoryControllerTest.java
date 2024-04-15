package com.bezkoder.spring.datajpa.controller;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.bezkoder.spring.datajpa.model.Category;
import com.bezkoder.spring.datajpa.model.CategoryLimit;
import com.bezkoder.spring.datajpa.request.ListCategory;
import com.bezkoder.spring.datajpa.request.ListCategoryRequest;
import com.bezkoder.spring.datajpa.response.Category.CreateCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.DeleteCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryByLimit;
import com.bezkoder.spring.datajpa.response.Category.ListCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.SaveAllCategoryResponse;
import com.bezkoder.spring.datajpa.response.Category.UpdateCategoryResponse;
import com.bezkoder.spring.datajpa.service.Impl.ServiceCategoryimpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
  
  private final static String URL_API_GETCATEGORY = "/api/category/getall";
  private final static String URL_API_CREATECATEGORY = "/api/category/create";
  private final static String URL_UPDATE_BY_ID = "/api/category/update/id";
  private final static String URL_SAVE_ALL = "/api/category/saveAll";
  private final static String URL_DELETE = "/api/category/delete";
  private final static String URL_UPDATE_ACTIVE = "/api/category/getactivity";
  private final static String URL_DELETE_ALL = "/api/category/deleteall";
  private final static String URL_UPDATE_ALL = "/api/category/updateall";
  private final static String URL_GET_CATEGORY_LIMIT = "/api/category/getCategoryLimit";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ServiceCategoryimpl serviceCategory;

  @Captor
  private ArgumentCaptor<Category> captorCategory;
  
  @Captor
  private ArgumentCaptor<Integer> captorId;
  
  @Captor
  private ArgumentCaptor<ListCategoryRequest> captorListCategoryRequest;

  @Captor
  private ArgumentCaptor<ListCategory> captorListIdCategory;

  ListCategoryRequest ListCategoryRequest = new ListCategoryRequest();
  List<Category> list = new ArrayList<>();
  final String pattern = "yyyy-MM-dd";
  final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

  @BeforeEach
  void initData() {
    ListCategoryRequest ListCategoryRequest = new ListCategoryRequest();
    for (int i = 0; i < 3; i++) {
      Category category = Category.builder()
    		  .foodName("Ga chien nuoc mam plus plus")
    		  .price(20000L)
    		  .build();
      list.add(category);
    }
    ListCategoryRequest.setListCategoryRequest(list);
  }

  @Test
  void testGetAllCategory_CaseNormal() throws Exception {
    ListCategoryResponse response = ListCategoryResponse.builder().status("200")
        .message("Successfully!").listdata(list).build();

    when(serviceCategory.getAll()).thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(get(URL_API_GETCATEGORY))
    	.andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Successfully!")))
        .andExpect(jsonPath("$.listdata", samePropertyValuesAs(response.getListdata())));
    verify(serviceCategory, times(1)).getAll();
  }

  @Test
  void testGetAllCategory_CaseNormal1() throws Exception {
    List<Category> listEmpty = new ArrayList<Category>();
    ListCategoryResponse response = ListCategoryResponse.builder().listdata(listEmpty).status("400")
        .message("No data!").build();

    when(serviceCategory.getAll()).thenReturn(ResponseEntity.status(400).body(response));

    mvc.perform(get(URL_API_GETCATEGORY)).andExpect(status().is(400))
        .andExpect(jsonPath("$.status", is("400"))).andExpect(jsonPath("$.message", is("No data!")))
        .andExpect(jsonPath("$.listdata", samePropertyValuesAs(response.getListdata())));
    verify(serviceCategory, times(1)).getAll();
  }

  @Test
  void testCreateCategory_CaseNormal() throws Exception {
    CreateCategoryResponse response =
        CreateCategoryResponse.builder().status("200").message("Created Successfully!").build();
    Category category = Category.builder().foodName("com chien duong chau")
        .createDate(simpleDateFormat.parse("2023-03-30")).price(25000L).active(1).build();

    when(serviceCategory.createCategory(category)).thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(post(URL_API_CREATECATEGORY).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(category))).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Created Successfully!")));
    verify(serviceCategory, times(1)).createCategory(captorCategory.capture());
    assertThat(captorCategory.getValue(), is(category));

  }

  @Test
  void testCreateCategory_CaseNormal1() throws Exception {
    CreateCategoryResponse response =
        CreateCategoryResponse.builder().status("200").message("FoodName is existed!").build();
    Category category = Category.builder().foodName("com chien duong chau")
        .createDate(simpleDateFormat.parse("2023-03-30")).price(25000L).active(1).build();

    when(serviceCategory.createCategory(category)).thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(post(URL_API_CREATECATEGORY).contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(category))).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("FoodName is existed!")));
    verify(serviceCategory, times(1)).createCategory(captorCategory.capture());
    assertThat(captorCategory.getValue(), is(category));
  }

  @Test
  void updateByIdEmployee_normal_01() throws Exception {
    Category category = Category.builder()
    							.foodName("canh cut chien toi")
    							.createDate(simpleDateFormat.parse("2023-04-03"))
    							.price(20000L)
    							.build();

    Category categoryDesire = Category.builder()
    								.foodName("canh cut chien toi")
    								.createDate(simpleDateFormat.parse("2023-04-03"))
    								.price(20000L)
    								.build();

    UpdateCategoryResponse response = UpdateCategoryResponse.builder()
    		.data(categoryDesire)
    		.status("200")
    		.message("Update successfully!")
    		.build();

    when(serviceCategory.updateCategory(33, category))
        .thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(post(URL_UPDATE_BY_ID.replace("id", "33"))
        .content(new ObjectMapper().writeValueAsString(category))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Update successfully!")))
        .andExpect(jsonPath("$.data.foodName", is("canh cut chien toi")));

    verify(serviceCategory, times(1)).updateCategory(captorId.capture(), captorCategory.capture());
    assertThat(captorId.getValue(), is(33));
    assertThat(captorCategory.getValue(), is(category));
  }

  @Test
  void updateByIdEmployee_normal_02() throws Exception {
    Category category = Category.builder().foodName("canh cut chien toi")
        .createDate(simpleDateFormat.parse("2023-04-03")).price(20000L).build();

    Category categoryDesire = Category.builder().foodName("canh cut chien toi")
        .createDate(simpleDateFormat.parse("2023-04-03")).price(20000L).build();

    UpdateCategoryResponse response = UpdateCategoryResponse.builder().data(categoryDesire)
        .status("204").message("Not found food by id!").build();

    when(serviceCategory.updateCategory(1, category))
        .thenReturn(ResponseEntity.status(204).body(response));

    mvc.perform(post(URL_UPDATE_BY_ID.replace("id", "1"))
        .content(new ObjectMapper().writeValueAsString(category))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(204))
        .andExpect(jsonPath("$.status", is("204")))
        .andExpect(jsonPath("$.message", is("Not found food by id!")));

    verify(serviceCategory, times(1)).updateCategory(captorId.capture(), captorCategory.capture());
    assertThat(captorId.getValue(), is(1));
    assertThat(captorCategory.getValue(), is(category));
  }

  @Test
  void updateByIdEmployee_normal_03() throws Exception {
    Category category = Category.builder().foodName("canh cut chien toi")
        .createDate(simpleDateFormat.parse("2023-04-03")).price(20000L).build();

    Category categoryDesire = Category.builder().foodName("canh cut chien toi")
        .createDate(simpleDateFormat.parse("2023-04-03")).price(20000L).build();

    UpdateCategoryResponse response = UpdateCategoryResponse.builder().data(categoryDesire)
        .status("400").message("Food Name is Exists!").build();

    when(serviceCategory.updateCategory(33, category))
        .thenReturn(ResponseEntity.status(400).body(response));

    mvc.perform(post(URL_UPDATE_BY_ID.replace("id", "33"))
        .content(new ObjectMapper().writeValueAsString(category))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(400))
        .andExpect(jsonPath("$.status", is("400")))
        .andExpect(jsonPath("$.message", is("Food Name is Exists!")));

    verify(serviceCategory, times(1)).updateCategory(captorId.capture(), captorCategory.capture());
    assertThat(captorId.getValue(), is(33));
    assertThat(captorCategory.getValue(), is(categoryDesire));
  }

  @Test
  void saveAllEmployee_normal_01() throws Exception {
    SaveAllCategoryResponse response =
        SaveAllCategoryResponse.builder().status("200").message("Save successfully").build();
    when(serviceCategory.saveAll(ListCategoryRequest))
        .thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(
        post(URL_SAVE_ALL).content(new ObjectMapper().writeValueAsString(ListCategoryRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Save successfully")));
    verify(serviceCategory, times(1)).saveAll(captorListCategoryRequest.capture());
    assertThat(captorListCategoryRequest.getValue(), is(ListCategoryRequest));
  }

  @Test
  void saveAllEmployee_normal_02() throws Exception {
    SaveAllCategoryResponse response =
        SaveAllCategoryResponse.builder().status("404").message("Save failed").build();
    when(serviceCategory.saveAll(ListCategoryRequest))
        .thenReturn(ResponseEntity.status(404).body(response));

    mvc.perform(
        post(URL_SAVE_ALL).content(new ObjectMapper().writeValueAsString(ListCategoryRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404)).andExpect(jsonPath("$.status", is("404")))
        .andExpect(jsonPath("$.message", is("Save failed")));
    verify(serviceCategory, times(1)).saveAll(captorListCategoryRequest.capture());
    assertThat(captorListCategoryRequest.getValue(), is(ListCategoryRequest));
  }

  @Test
  void deleteAllEmployee_normal_01() throws Exception {
    List<Integer> listId = new ArrayList<>();
    listId.add(33);
    listId.add(34);
    listId.add(35);
    ListCategory lstCategoryId = new ListCategory();
    lstCategoryId.setCategoryId(listId);
    DeleteCategoryResponse response = DeleteCategoryResponse.builder().status("200").message("Delete Successfully!").build();
    
    when(serviceCategory.deleteCategoryById(any(ListCategory.class))).thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(delete(URL_DELETE).content(new ObjectMapper().writeValueAsString(lstCategoryId))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    verify(serviceCategory, times(1)).deleteCategoryById(captorListIdCategory.capture());
    assertThat(captorListIdCategory.getValue().getCategoryId(), samePropertyValuesAs(lstCategoryId.getCategoryId()));

  }

  @Test
  void updateActiveEmployee_normal_01() throws Exception {
    List<Category> listdata = list;
    ListCategoryResponse deleteCategoryResponse = ListCategoryResponse.builder().status("200")
        .listdata(listdata).message("Successfully!").build();
    when(serviceCategory.selectAllCategory())
        .thenReturn(ResponseEntity.ok().body(deleteCategoryResponse));

    mvc.perform(get(URL_UPDATE_ACTIVE)).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Successfully!")));
    
    verify(serviceCategory, times(1)).selectAllCategory();
  }

  @Test
  void deleteAll_normal_01() throws Exception {
    DeleteCategoryResponse deleteCategoryResponse =
        DeleteCategoryResponse.builder().status("200").message("Delete Successful").build();
    when(serviceCategory.deleteAllCategory())
        .thenReturn(ResponseEntity.ok().body(deleteCategoryResponse));

    mvc.perform(delete(URL_DELETE_ALL)).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Delete Successful")));
    verify(serviceCategory, times(1)).deleteAllCategory();
  }

  @Test
  void updateAll_normal_01() throws Exception {
    UpdateCategoryResponse updateCategoryResponse =
        UpdateCategoryResponse.builder().status("200").message("Update Successful").build();
    when(serviceCategory.updateAllCategory())
        .thenReturn(ResponseEntity.ok().body(updateCategoryResponse));

    mvc.perform(post(URL_UPDATE_ALL)).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200")))
        .andExpect(jsonPath("$.message", is("Update Successful")));
    verify(serviceCategory, times(1)).updateAllCategory();
  }

  @Test
  void getCategoryLimit_normal_01() throws Exception {
    List<CategoryLimit> listdata = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      CategoryLimit categoryLimit =
          CategoryLimit.builder().foodName("Ga chien nuoc mam plus plus").price(20000L).build();
      listdata.add(categoryLimit);
    }
    ListCategoryByLimit response =
        ListCategoryByLimit.builder().status("200").message("Data").listdata(listdata).build();
    when(serviceCategory.getCategoryLimitByDate()).thenReturn(ResponseEntity.ok().body(response));

    mvc.perform(get(URL_GET_CATEGORY_LIMIT)).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("200"))).andExpect(jsonPath("$.message", is("Data")));
    verify(serviceCategory, times(1)).getCategoryLimitByDate();
  }
}

package com.bezkoder.spring.datajpa.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.bezkoder.spring.datajpa.service.Impl.ServiceCategoryimpl;

@AutoConfigureMockMvc
@SpringBootTest
public class CategoryServiceTest {

  private static final int Id_Category = 1;

  @InjectMocks
  private ServiceCategoryimpl serviceCategory;

  @Mock
  private CategoryRepository categoryRepository;

  @Captor
  private ArgumentCaptor<Category> captorCategory;

  @Captor
  private ArgumentCaptor<Integer> captorid;

  @Captor
  private ArgumentCaptor<ListCategory> captorLstNummberId;

  @Captor
  private ArgumentCaptor<ListCategoryRequest> captorListCategoryRequest;

  @Captor
  private ArgumentCaptor<String> captorFoodName;

  List<Category> listStart = new ArrayList<>();

  ListCategory lstCategoryId = new ListCategory();

  @BeforeEach
  void initData() {
    for (int i = 1; i <= 3; i++) {
      Category category = Category.builder().id(i).foodName("Ga chien nuoc mam plus plus" + i)
          .price(20000L).build();
      listStart.add(category);
    }
  }

  @Test
  public void testGetAll_normal_01() throws Exception {
    ListCategoryResponse response = ListCategoryResponse.builder().listdata(listStart).status("200")
        .message("Successfully!").build();
    when(categoryRepository.getAllCategory()).thenReturn(listStart);
    ResponseEntity<ListCategoryResponse> listCategoryResponse = serviceCategory.getAll();

    verify(categoryRepository, times(1)).getAllCategory();

    assertThat(listCategoryResponse.getBody().getListdata(),
        samePropertyValuesAs(response.getListdata()));

  }

  @Test
  public void testUpdateCategory_normal_01() {
    Category category = Category.builder().foodName("Ga chien nuoc mam").price(20000L).build();

    Category categoryByFoodName =
        Category.builder().foodName("Ga chien nuoc mam").price(20000L).build();

    when(categoryRepository.getCategoryById(Id_Category)).thenReturn(category);
    when(categoryRepository.getCategoryNotInId(category.getFoodName(), Id_Category))
        .thenReturn(categoryByFoodName);
    ResponseEntity<UpdateCategoryResponse> updateCategoryResponse =
        serviceCategory.updateCategory(Id_Category, category);

    verify(categoryRepository, times(1)).getCategoryById(captorid.capture());
    verify(categoryRepository, times(1)).getCategoryNotInId(captorFoodName.capture(),
        captorid.capture());
    assertThat(captorid.getValue(), is(1));
    assertThat(captorFoodName.getValue(), is("Ga chien nuoc mam"));

    assertThat(updateCategoryResponse.getStatusCode(), is(HttpStatus.NO_CONTENT));
  }

  @Test
  public void testUpdateCategory_normal_02() {
    Category category = Category.builder().foodName("Ga chien nuoc mam").price(20000L).build();

    when(categoryRepository.getCategoryById(Id_Category)).thenReturn(category);
    when(categoryRepository.getCategoryNotInId(category.getFoodName(), Id_Category))
        .thenReturn(null);
    ResponseEntity<UpdateCategoryResponse> updateCategoryResponse =
        serviceCategory.updateCategory(Id_Category, category);

    verify(categoryRepository, times(1)).getCategoryById(captorid.capture());
    verify(categoryRepository, times(1)).getCategoryNotInId(captorFoodName.capture(),
        captorid.capture());
    assertThat(captorid.getValue(), is(1));
    assertThat(captorFoodName.getValue(), is("Ga chien nuoc mam"));
    assertThat(updateCategoryResponse.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testUpdateCategory_normal_03() {
    Category category = Category.builder().foodName("Ga chien nuoc mam").price(20000L).build();
    when(categoryRepository.getCategoryById(Id_Category)).thenReturn(null);
    ResponseEntity<UpdateCategoryResponse> updateCategoryResponse =
        serviceCategory.updateCategory(Id_Category, category);
    verify(categoryRepository, times(1)).getCategoryById(captorid.capture());
    assertThat(captorid.getValue(), is(1));
    assertThat(updateCategoryResponse.getStatusCode(), is(HttpStatus.NO_CONTENT));
  }

  @Test
  public void testDeleteCategoryByIdCategory_normal_01() {
    List<Integer> lstNummberId = Arrays.asList(1, 2, 3);
    lstCategoryId.setCategoryId(lstNummberId);

    List<Category> listCateogry = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      Category category = Category.builder().id(i).foodName("Ga chien nuoc mam plus plus" + i)
          .price(20000L).build();
      listCateogry.add(category);
    }
    when(categoryRepository.selectFor(lstCategoryId)).thenReturn(listCateogry);
    when(categoryRepository.deleteFor(lstCategoryId)).thenReturn(true);
    ResponseEntity<DeleteCategoryResponse> deleteCategoryResponse =
        serviceCategory.deleteCategoryById(lstCategoryId);
    verify(categoryRepository, times(1)).selectFor(captorLstNummberId.capture());
    verify(categoryRepository, times(1)).deleteFor(captorLstNummberId.capture());
    assertThat(captorLstNummberId.getValue(), samePropertyValuesAs(lstCategoryId));
    assertThat(deleteCategoryResponse.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testDeleteCategoryByIdCategory_normal_02() {
    List<Integer> lstNummberId = Arrays.asList(1, 2, 3, 4);
    lstCategoryId.setCategoryId(lstNummberId);

    List<Category> listCateogry = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      Category category = Category.builder().id(i).foodName("Ga chien nuoc mam plus plus" + i)
          .price(20000L).build();
      listCateogry.add(category);
    }
    when(categoryRepository.selectFor(lstCategoryId)).thenReturn(listCateogry);
    ResponseEntity<DeleteCategoryResponse> deleteCategoryResponse =
        serviceCategory.deleteCategoryById(lstCategoryId);
    verify(categoryRepository, times(1)).selectFor(captorLstNummberId.capture());
    assertThat(captorLstNummberId.getValue(), samePropertyValuesAs(lstCategoryId));
    assertThat(deleteCategoryResponse.getStatusCode(), is(HttpStatus.NOT_IMPLEMENTED));
  }

  @Test
  public void testSelectAllCategoryByActive_normal_01() {
    List<Category> listCateogry = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      Category category = Category.builder().id(i).foodName("Ga chien nuoc mam plus plus" + i)
          .price(20000L).active(1).build();
      listCateogry.add(category);
    }
    when(categoryRepository.selectAllCategory()).thenReturn(listCateogry);
    ResponseEntity<ListCategoryResponse> listCategoryResponse = serviceCategory.selectAllCategory();
    verify(categoryRepository, times(1)).selectAllCategory();
    assertThat(listCategoryResponse.getStatusCode(), is(HttpStatus.OK));
    assertThat(listCategoryResponse.getBody().getListdata(), is(listCateogry));
  }

  @Test
  public void testdeleteAllCategory_normal_01() {
    when(categoryRepository.deleteAllCategory()).thenReturn(true);
    ResponseEntity<DeleteCategoryResponse> deleteCategoryResponse =
        serviceCategory.deleteAllCategory();
    verify(categoryRepository, times(1)).deleteAllCategory();
    assertThat(deleteCategoryResponse.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testUpdateAllCategory_normal_01() {
    when(categoryRepository.updateAllCategory()).thenReturn(true);
    ResponseEntity<UpdateCategoryResponse> updateCategoryResponse =
        serviceCategory.updateAllCategory();
    verify(categoryRepository, times(1)).updateAllCategory();
    assertThat(updateCategoryResponse.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testCreateCategory_normal_01() {
    Category category =
        Category.builder().foodName("Ga chien nuoc mam plus plus").price(20000L).active(1).build();
    Category categoryExist =
        Category.builder().foodName("Ga chien nuoc mam plus plus").price(20000L).active(1).build();
    when(categoryRepository.getCategory(category.getFoodName())).thenReturn(categoryExist);
    ResponseEntity<CreateCategoryResponse> createCategoryResponse =
        serviceCategory.createCategory(category);
    verify(categoryRepository, times(1)).getCategory(captorFoodName.capture());
    assertThat(captorFoodName.getValue(), is("Ga chien nuoc mam plus plus"));
    assertThat(createCategoryResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testCreateCategory_normal_02() {
    Category category =
        Category.builder().foodName("Ga chien nuoc mam plus plus").price(20000L).active(1).build();
    Category categoryIsEmpty = null;
    when(categoryRepository.getCategory(category.getFoodName())).thenReturn(categoryIsEmpty);
    ResponseEntity<CreateCategoryResponse> createCategoryResponse =
        serviceCategory.createCategory(category);
    verify(categoryRepository, times(1)).getCategory(captorFoodName.capture());
    assertThat(captorFoodName.getValue(), is("Ga chien nuoc mam plus plus"));
    assertThat(createCategoryResponse.getStatusCode(), is(HttpStatus.CREATED));
  }

  @Test
  public void testSaveAll_normal_01() {
    ListCategoryRequest listCategoryRequest = new ListCategoryRequest();
    listCategoryRequest.setListCategoryRequest(listStart);
    when(categoryRepository.SelectCategoryByFoodName(listCategoryRequest)).thenReturn(listStart);
    ResponseEntity<SaveAllCategoryResponse> saveAllCategoryResponse =
        serviceCategory.saveAll(listCategoryRequest);
    verify(categoryRepository, times(1)).SelectCategoryByFoodName(captorListCategoryRequest.capture());
    assertThat(captorListCategoryRequest.getValue(), samePropertyValuesAs(listCategoryRequest));
    assertThat(saveAllCategoryResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testSaveAll_normal_02() {
    ListCategoryRequest listCategoryRequest = new ListCategoryRequest();
    listCategoryRequest.setListCategoryRequest(listStart);
    List<Category> listIsEmpty = isNull();
    when(categoryRepository.SelectCategoryByFoodName(listCategoryRequest)).thenReturn(listIsEmpty);
    when(categoryRepository.saveCategoryByFoodName(listCategoryRequest)).thenReturn(true);
    ResponseEntity<SaveAllCategoryResponse> saveAllCategoryResponse =
        serviceCategory.saveAll(listCategoryRequest);
    
    verify(categoryRepository, times(1)).SelectCategoryByFoodName(captorListCategoryRequest.capture());
    assertThat(captorListCategoryRequest.getValue(), samePropertyValuesAs(listCategoryRequest));
    verify(categoryRepository, times(1)).saveCategoryByFoodName(captorListCategoryRequest.capture());
    assertThat(captorListCategoryRequest.getValue(), samePropertyValuesAs(listCategoryRequest));
    assertThat(saveAllCategoryResponse.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testgetCategoryLimitByDate_normal_01() {
    List<CategoryLimit> categoryList = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      CategoryLimit categoryLimit = CategoryLimit.builder().id(i)
          .foodName("Ga chien nuoc mam plus plus" + i).price(20000L).build();
      categoryList.add(categoryLimit);
    }
    when(categoryRepository.getCategoryLimitByDate()).thenReturn(categoryList);
    ResponseEntity<ListCategoryByLimit> listCategoryByLimit =
        serviceCategory.getCategoryLimitByDate();
    verify(categoryRepository, times(1)).getCategoryLimitByDate();
    assertThat(listCategoryByLimit.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testgetCategoryLimitByDate_normal_02() {
    List<CategoryLimit> categoryList = new ArrayList<>();
    when(categoryRepository.getCategoryLimitByDate()).thenReturn(categoryList);
    ResponseEntity<ListCategoryByLimit> listCategoryByLimit =
        serviceCategory.getCategoryLimitByDate();
    verify(categoryRepository, times(1)).getCategoryLimitByDate();
    assertThat(listCategoryByLimit.getStatusCode(), is(HttpStatus.NO_CONTENT));
  }
}

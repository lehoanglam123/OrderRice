package com.bezkoder.spring.datajpa.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hpsf.Array;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bezkoder.spring.datajpa.model.Category;
import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.repository.EmployeeRepository;
import com.bezkoder.spring.datajpa.response.Category.CreateCategoryResponse;
import com.bezkoder.spring.datajpa.response.Employee.CreateEmployeeResponse;
import com.bezkoder.spring.datajpa.response.Employee.LoginEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;


@AutoConfigureMockMvc
@SpringBootTest
public class LoginServiceTest {
	
	@Autowired
	private ServiceEmployee serviceEmployee;
	
	@MockBean
	private EmployeeRepository employeeRepository;
	
	List<Category> list = new ArrayList<>();
	
	@Captor
	ArgumentCaptor<Employee> employeeCaptur;
	@Captor
	ArgumentCaptor<String> employeeCapturAccount;

	
	@BeforeEach
	void initData() throws Exception
	{
		
	}
	@Test 
	void testServiceRegister_CaseNormal() throws Exception 
	{
		Optional<Employee> employee = Optional.ofNullable(Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password("123456789")
				.build());
//		when(employeeRepository.getEmployeeByAccount(employee.get())).thenReturn(employee);
//		ResponseEntity<CreateEmployeeResponse> createResponseEntity = serviceEmployee.createEmployee(employee.get());
//		assertEquals(HttpStatus.BAD_REQUEST, createResponseEntity.getStatusCode());
		
		when(employeeRepository.getEmployeeByAccount(employee.get())).thenReturn(employee);
		ResponseEntity<CreateEmployeeResponse> createResponseEntity = serviceEmployee.createEmployee(employee.get());
		verify(employeeRepository,times(1)).getEmployeeByAccount(employeeCaptur.capture());
		assertThat(employeeCaptur.getValue(),is(employee.get()));
		assertThat(createResponseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}
	@Test
	void testServiceRegister_CaseNormal01() throws Exception
	{
		
		Optional<Employee> employee = Optional.ofNullable(Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password("123456789")
				.build());
		//Optional<Employee> opEmployeeIsEmpty = Optional.empty();
		
//		when(employeeRepository.getEmployeeByAccount(employee.get())).thenReturn(employee.empty());
//		when(employeeRepository.createNewEmployee(employee.get())).thenReturn(true);
//		when(employeeRepository.getEmployeeNew()).thenReturn(employee);
//		ResponseEntity<CreateEmployeeResponse> createResponseEntity = serviceEmployee.createEmployee(employee.get());
//		assertEquals(HttpStatus.OK, createResponseEntity.getStatusCode());
		
		
		Optional<Employee> employeeEmpty = Optional.empty();
		when(employeeRepository.getEmployeeByAccount(employee.get())).thenReturn(employeeEmpty);
		//when(employeeRepository.createNewEmployee(employee.get())).thenReturn(true);
		when(employeeRepository.getEmployeeNew()).thenReturn(employee);
		ResponseEntity<CreateEmployeeResponse> createResponseEntity = serviceEmployee.createEmployee(employee.get());
		verify(employeeRepository, times(1)).getEmployeeByAccount(employeeCaptur.capture());
		verify(employeeRepository, times(1)).createNewEmployee(employeeCaptur.capture());
		verify(employeeRepository, times(1)).getEmployeeNew();
		assertThat(employeeCaptur.getValue(), is(employee.get()));
		assertThat(createResponseEntity.getStatusCode(),is(HttpStatus.OK));
	}
	
	@Test
	void testServiceLogin_CaseNormal() throws Exception
	{
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String pwd = encoder.encode("12345678");
		//String hash = BCrypt.hashpw(pwd,BCrypt.gensalt()+pwd);
		//String pass = BCrypt.checkpw(pwd, hash);
		Optional<Employee> employee = Optional.ofNullable(Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password(pwd)
				.build());
		
		Employee employeeInput = Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password("12345678")
				.build();
		
//		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
//		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
//		assertEquals(HttpStatus.OK, loginResponseEntity.getStatusCode());
		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
		verify(employeeRepository, times(1)).getEmployeeByAccountAndPass(employeeCapturAccount.capture());
		assertThat(employeeCapturAccount.getValue(), is(employee.get().getAccount()));
		assertThat(loginResponseEntity.getStatusCode(), is(HttpStatus.OK));
	}
//	
//	@Test
//	void testServiceLogin_CaseNormal01() throws Exception
//	{
//		Optional<Employee> employee = Optional.empty();
//		
//		Employee employeeInput = Employee.builder()
//				.fullName(null)
//				.department(null)
//				.account("bnv1")
//				.phone(null)
//				.password("123456789")
//				.build();
//		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
//		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
//		assertEquals(HttpStatus.NO_CONTENT, loginResponseEntity.getStatusCode());
//	}
//	
//	@Test
//	void testServiceLogin_CaseNorma02() throws Exception
//	{
//		PasswordEncoder encoder = new BCryptPasswordEncoder();
//		String pwd = encoder.encode("12345678");
//		Optional<Employee> employee = Optional.ofNullable(Employee.builder()
//				.fullName("Nguyễn Văn B")
//				.department("kk")
//				.account("bnv1")
//				.phone("0123456789")
//				.password(pwd)
//				.build());
//		
//		Employee employeeInput = Employee.builder()
//				.fullName(null)
//				.department(null)
//				.account("bnv1")
//				.phone(null)
//				.password("12345675")
//				.build();
//		
//		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
//		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
//		assertEquals(HttpStatus.NO_CONTENT, loginResponseEntity.getStatusCode());
//	}
//	
	
//	@Test
//	public void testUpdateCategory_normal_01() {
//		Category category = Category.builder().foodName("Ga chien nuoc mam").price(20000L).build();
//
//		Category categoryByFoodName = Category.builder().foodName("Ga chien nuoc mam").price(20000L).build();
//
//		when(categoryRepository.getCategoryById(Id_Category)).thenReturn(category);
//		when(categoryRepository.getCategoryNotInId(category.getFoodName(), Id_Category)).thenReturn(categoryByFoodName);
//		ResponseEntity<UpdateCategoryResponse> updateCategoryResponse = serviceCategory.updateCategory(Id_Category,
//				category);
//		
//		verify(categoryRepository, times(1)).getCategoryById(captorid.capture());
//		verify(categoryRepository, times(1)).getCategoryNotInId(captorFoodName.capture(), captorid.capture());
//		assertThat(captorid.getValue(), is(1));
//		assertThat(captorFoodName.getValue(), is("Ga chien nuoc mam"));
//
//		assertThat(updateCategoryResponse.getStatusCode(), is(HttpStatus.NO_CONTENT));
//	}
}
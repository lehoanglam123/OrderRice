package com.bezkoder.spring.datajpa.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
public class ServiceEmployeeTest {
	
	@Autowired
	private ServiceEmployee serviceEmployee;
	
	@MockBean
	private EmployeeRepository employeeRepository;
	@Captor
	ArgumentCaptor<Employee> employeeCapture;
	@Captor
	ArgumentCaptor<String> employeeCaptureAccount;
	
	List<Category> list = new ArrayList<>();
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
		ResponseEntity<CreateEmployeeResponse> createEmployeeResponse = serviceEmployee.createEmployee(employee.get());
		verify(employeeRepository,times(1)).getEmployeeByAccount(employeeCapture.capture());
		assertThat(employeeCapture.getValue(), is(employee.get()));
		assertThat(createEmployeeResponse.getStatusCode(), is(HttpStatus.BAD_REQUEST));
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
		when(employeeRepository.createNewEmployee(employee.get())).thenReturn(true);
		when(employeeRepository.getEmployeeNew()).thenReturn(employee);
		ResponseEntity<CreateEmployeeResponse> createResponseEntity = serviceEmployee.createEmployee(employee.get());
		verify(employeeRepository, times(1)).getEmployeeByAccount(employeeCapture.capture());
		verify(employeeRepository,times(1)).createNewEmployee(employeeCapture.capture());
		verify(employeeRepository, times(1)).getEmployeeNew();
		assertThat(employeeCapture.getValue(),is(employee.get()));
		assertThat(createResponseEntity.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	void testLoginService_CaseNormal() throws Exception
	{
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String pass = encoder.encode("12345678");
		Optional<Employee> employee = Optional.ofNullable(Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password(pass)
				.build());
		Employee employeeInput = Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password("12345678")
				.build();
		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
		verify(employeeRepository,times(1)).getEmployeeByAccountAndPass(employeeCaptureAccount.capture());
		assertThat(employeeCaptureAccount.getValue(), is(employee.get().getAccount()));
		assertThat(loginResponseEntity.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	void testLoginService_CaseNormal01() throws Exception
	{
		
		Optional<Employee> employee = Optional.empty();
		Employee employeeInput = Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("opi")
				.phone("0123456789")
				.password("12345678")
				.build();
		//when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount()).thenReturn(employee.get()));
		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
		verify(employeeRepository,times(1)).getEmployeeByAccountAndPass(employeeCaptureAccount.capture());
		assertThat(employeeCaptureAccount.getValue(), is(employeeInput.getAccount()));
		assertThat(loginResponseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));
	}
	
	@Test
	void testLoginService_CaseNormal02() throws Exception
	{
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String pass = encoder.encode("12345678");
		Optional<Employee> employee = Optional.ofNullable(Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password(pass)
				.build());
		Employee employeeInput = Employee.builder()
				.fullName("Nguyễn Văn B")
				.department("kk")
				.account("bnv1")
				.phone("0123456789")
				.password("12348")
				.build();
		when(employeeRepository.getEmployeeByAccountAndPass(employeeInput.getAccount())).thenReturn(employee);
		ResponseEntity<LoginEmployeeResponse> loginResponseEntity = serviceEmployee.checkExistAcc(employeeInput);
		verify(employeeRepository, times(1)).getEmployeeByAccountAndPass(employeeCaptureAccount.capture());
		assertThat(employeeCaptureAccount.getValue(), is(employee.get().getAccount()));
		assertThat(loginResponseEntity.getStatusCode(), is(HttpStatus.NO_CONTENT));
	}
}

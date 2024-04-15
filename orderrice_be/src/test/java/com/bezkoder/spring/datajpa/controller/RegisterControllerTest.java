package com.bezkoder.spring.datajpa.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.response.Employee.CreateEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
	private final String URL_API_REGISTER = "/api/register/create";
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	private ServiceEmployee serviceEmployee;
	
	@Captor
	private ArgumentCaptor<Employee>  employeeCapture;
	
	@BeforeEach
	void initData() {}
	
	@Test
	void testRegisterEmployee_CaseNormal() throws Exception{
		
		Employee employee = Employee.builder()
				.fullName("Nguyễn Văn An")
				.department("cc")
				.account("anv1")
				.phone("0123456789")
				.password("12345678")
				.build();
		CreateEmployeeResponse response = CreateEmployeeResponse.builder().data(employee).status("200").message("Create account successfully").build();
//		
		when(serviceEmployee.createEmployee(employee))
		.thenReturn(ResponseEntity.ok().body(response));
		
		mvc.perform(post(URL_API_REGISTER)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(employee)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(jsonPath("$.message", is("Create account successfully")))
				.andExpect(jsonPath("$.data.fullName", is("Nguyễn Văn An")))
				.andExpect(jsonPath("$.data.department", is("cc")))
				.andExpect(jsonPath("$.data.account", is("anv1")))
				.andExpect(jsonPath("$.data.phone", is("0123456789")))
				.andExpect(jsonPath("$.data.password", is("12345678")));
				
		verify(serviceEmployee, times(1)).createEmployee(employeeCapture.capture());
		assertThat(employeeCapture.getValue(), is(employee));
    	
		
//		verify(serviceEmployee,times(1)).createEmployee(responseBody.capture());
//		assertThat(responseBody.getValue(),samePropertyValuesAs(employee));
	}

	private String asJsonString(final Object obj) 
	{
		try 
		{
			return new ObjectMapper().writeValueAsString(obj);
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	@Test
	void testRegisterEmployee_CaseNormal1() throws Exception
	{
		
		Employee employee = Employee.builder()
				.fullName("Nguyễn Văn An")
				.department("cc")
				.account("anv1")
				.phone("0123456789")
				.password("12345678")
				.build();
		CreateEmployeeResponse response = CreateEmployeeResponse.builder().data(employee).status("400").message("Account already exists").build();
		when(serviceEmployee.createEmployee(employee))
		.thenReturn(ResponseEntity.badRequest().body(response));
		
		mvc.perform(MockMvcRequestBuilders.post(URL_API_REGISTER)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(employee)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.status", is("400")))
				.andExpect(jsonPath("$.message", is("Account already exists")))
				.andExpect(jsonPath("$.data.fullName", is("Nguyễn Văn An")))
				.andExpect(jsonPath("$.data.department", is("cc")))
				.andExpect(jsonPath("$.data.account", is("anv1")))
				.andExpect(jsonPath("$.data.phone", is("0123456789")))
				.andExpect(jsonPath("$.data.password", is("12345678")));
		verify(serviceEmployee, times(1)).createEmployee(employeeCapture.capture());
    	assertThat(employeeCapture.getValue(), is(employee));
		
	}
}


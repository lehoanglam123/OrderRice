package com.bezkoder.spring.datajpa.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.response.Employee.LoginEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LoginController.class)

public class LoginControllerTest {
	private String URL_API_LOGIN ="/api/login";
@MockBean
private ServiceEmployee serviceEmployee;

@Autowired 
private MockMvc mvc;

@Captor
ArgumentCaptor<Employee> employeeCapture;

@Test
	void testLoginController_CaseNormal() throws Exception
	{
		Employee employee = Employee.builder()
			.fullName("Nguyễn Văn An")
			.department("cc")
			.account("anv1")
			.phone("0123456789")
			.password("12345678")
			.build();
		LoginEmployeeResponse response = LoginEmployeeResponse.builder().data(Optional.of(employee))
				.status("200").message("Login successfully!").build();
		when(serviceEmployee.checkExistAcc(employee)).thenReturn(ResponseEntity.ok().body(response));
		mvc.perform(post(URL_API_LOGIN)
			.content(new ObjectMapper().writeValueAsString(employee))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.status", is("200")))
			.andExpect(jsonPath("$.message", is("Login successfully!")))
			.andExpect(jsonPath("$.data.account", is("anv1")))
			.andExpect(jsonPath("$.data.password", is("12345678")))
			;
		verify(serviceEmployee, times(1)).checkExistAcc(employeeCapture.capture());
		assertThat(employeeCapture.getValue(), is(employee));
	}

@Test
void testLoginController_CaseNormal01() throws Exception
{
	Employee employee = Employee.builder()
		.fullName("Nguyễn Văn An")
		.department("cc")
		.account("anv1")
		.phone("0123456789")
		.password("12345678")
		.build();
	LoginEmployeeResponse response = LoginEmployeeResponse.builder().data(Optional.of(employee))
			.status("204").message("Login Failure!").build();
	when(serviceEmployee.checkExistAcc(employee)).thenReturn(ResponseEntity.status(204).body(response));
	mvc.perform(post(URL_API_LOGIN)
		.content(new ObjectMapper().writeValueAsString(employee))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andExpect(jsonPath("$.status", is("204")))
		.andExpect(jsonPath("$.message", is("Login Failure!")))
		.andExpect(jsonPath("$.data.account", is("anv1")))
		.andExpect(jsonPath("$.data.password", is("12345678")))
		;
	verify(serviceEmployee, times(1)).checkExistAcc(employeeCapture.capture());
	assertThat(employeeCapture.getValue(), is(employee));
}

}


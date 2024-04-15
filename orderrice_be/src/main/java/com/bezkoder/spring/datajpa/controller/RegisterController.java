package com.bezkoder.spring.datajpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.response.Employee.CreateEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/register")
@ResponseBody
public class RegisterController {
	@Autowired
	private ServiceEmployee serviceEmployee;

	@PostMapping("/create")
	public ResponseEntity<CreateEmployeeResponse> createNewEmployee(@Valid @RequestBody Employee employee) {
			return serviceEmployee.createEmployee(employee);
	}
}

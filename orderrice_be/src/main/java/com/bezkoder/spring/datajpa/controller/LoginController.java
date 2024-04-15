package com.bezkoder.spring.datajpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.response.Employee.LoginEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@ResponseBody
public class LoginController {
	@Autowired
	private ServiceEmployee serviceEmployee;

	@PostMapping("/login")
	public ResponseEntity<LoginEmployeeResponse> signIn(@RequestBody Employee employee)
	{
		return serviceEmployee.checkExistAcc(employee);
	}
}

package com.bezkoder.spring.datajpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.response.Employee.ListEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employee")
@ResponseBody
public class EmployeeController {
	@Autowired
	private ServiceEmployee serviceEmployee;

	@GetMapping("/getall")
	public ResponseEntity<ListEmployeeResponse> getAllEmployee() {
		return serviceEmployee.getAllEmployee();
	}
}

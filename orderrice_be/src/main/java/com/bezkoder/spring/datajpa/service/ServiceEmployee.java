package com.bezkoder.spring.datajpa.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.response.Employee.CreateEmployeeResponse;
import com.bezkoder.spring.datajpa.response.Employee.ListEmployeeResponse;
import com.bezkoder.spring.datajpa.response.Employee.LoginEmployeeResponse;

@Service
public interface ServiceEmployee {

	public ResponseEntity<ListEmployeeResponse> getAllEmployee();

	
	public ResponseEntity<LoginEmployeeResponse> checkExistAcc(Employee employee);

	ResponseEntity<CreateEmployeeResponse> createEmployee(Employee employee);
}

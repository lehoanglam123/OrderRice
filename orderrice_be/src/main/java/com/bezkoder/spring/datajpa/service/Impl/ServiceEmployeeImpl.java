package com.bezkoder.spring.datajpa.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.repository.EmployeeRepository;
import com.bezkoder.spring.datajpa.response.Employee.CreateEmployeeResponse;
import com.bezkoder.spring.datajpa.response.Employee.ListEmployeeResponse;
import com.bezkoder.spring.datajpa.response.Employee.LoginEmployeeResponse;
import com.bezkoder.spring.datajpa.service.ServiceEmployee;

@Service
public class ServiceEmployeeImpl implements ServiceEmployee {
	@Autowired
	private EmployeeRepository employeeRepository;

	PasswordEncoder encoder = new BCryptPasswordEncoder();
	@Override
	public ResponseEntity<ListEmployeeResponse> getAllEmployee() {
		try {
			List<Employee> employee = employeeRepository.getAllEmployee();
			ListEmployeeResponse response = ListEmployeeResponse.builder().data(employee).status("200")
					.message("Successfully!").build();
			return new ResponseEntity<ListEmployeeResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			ListEmployeeResponse response = ListEmployeeResponse.builder().status("404").message("No data!").build();
			return new ResponseEntity<ListEmployeeResponse>(response, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<LoginEmployeeResponse> checkExistAcc(Employee employee) {
		Optional<Employee> employeeAccount = employeeRepository.getEmployeeByAccountAndPass(employee.getAccount());
		if (employeeAccount.isPresent()){
			if (encoder.matches(employee.getPassword(), employeeAccount.get().getPassword())) {
				LoginEmployeeResponse response = LoginEmployeeResponse.builder().data(employeeAccount).status("200")
						.message("Login successfully!").build();
				return new ResponseEntity<LoginEmployeeResponse>(response, HttpStatus.OK);
			}
		}
		LoginEmployeeResponse response = LoginEmployeeResponse.builder().status("204").message("Login Failure!")
				.build();
		return new ResponseEntity<LoginEmployeeResponse>(response, HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<CreateEmployeeResponse> createEmployee(Employee employee) {
	
			Optional<Employee> eml = employeeRepository.getEmployeeByAccount(employee);
			if (eml.isPresent()) {
				CreateEmployeeResponse registerEmployeeResponseFail = CreateEmployeeResponse.builder().status("400")
						.message("Account already exists").build();
				return new ResponseEntity<CreateEmployeeResponse>(registerEmployeeResponseFail, HttpStatus.BAD_REQUEST);
			}
			String pwd = encoder.encode(employee.getPassword());
			employee.setPassword(pwd);
			employeeRepository.createNewEmployee(employee);
			Optional<Employee> employeeNew = employeeRepository.getEmployeeNew();
			CreateEmployeeResponse registerEmployeeResponseSuccessfully = CreateEmployeeResponse.builder()
					.data(employeeNew.get()).status("200").message("Create account successfully").build();
			return new ResponseEntity<CreateEmployeeResponse>(registerEmployeeResponseSuccessfully, HttpStatus.OK);
		
	}
}

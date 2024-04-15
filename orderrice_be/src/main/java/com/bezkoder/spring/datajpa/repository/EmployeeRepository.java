package com.bezkoder.spring.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.bezkoder.spring.datajpa.model.Employee;

@Mapper
public interface EmployeeRepository {

	List<Employee> getAllEmployee();
	Optional<Employee> getEmployeeByAccountAndPass(String account);
	boolean createNewEmployee(Employee employee);

	Optional<Employee> getEmployeeByAccount(Employee account);
	
	Optional<Employee> getEmployeeNew();
}

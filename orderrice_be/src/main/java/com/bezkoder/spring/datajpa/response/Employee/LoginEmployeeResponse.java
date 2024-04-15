package com.bezkoder.spring.datajpa.response.Employee;

import com.bezkoder.spring.datajpa.model.Employee;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class LoginEmployeeResponse {
    private String status;
    private String message;
    private Optional<Employee> data;
}

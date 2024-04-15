package com.bezkoder.spring.datajpa.response.Employee;

import com.bezkoder.spring.datajpa.model.Employee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmployeeResponse {
    private String status;
    private String message;
    private Employee data;
}

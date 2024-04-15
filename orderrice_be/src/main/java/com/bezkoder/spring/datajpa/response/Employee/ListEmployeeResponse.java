package com.bezkoder.spring.datajpa.response.Employee;

import com.bezkoder.spring.datajpa.model.Employee;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListEmployeeResponse {
    private String status;
    private String message;

    private List<Employee> data;
}

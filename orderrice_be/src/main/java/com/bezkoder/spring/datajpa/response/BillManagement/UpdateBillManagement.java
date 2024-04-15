package com.bezkoder.spring.datajpa.response.BillManagement;

import com.bezkoder.spring.datajpa.model.BillManagement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBillManagement {
    private String status;
    private String message;

    private BillManagement data;
}

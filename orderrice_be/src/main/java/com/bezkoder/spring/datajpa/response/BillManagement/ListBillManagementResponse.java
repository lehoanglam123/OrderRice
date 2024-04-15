package com.bezkoder.spring.datajpa.response.BillManagement;

import com.bezkoder.spring.datajpa.model.BillManagementById;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListBillManagementResponse {
    private String status;
    private  String message;

    private List<BillManagementById> data;
}

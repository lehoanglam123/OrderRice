package com.bezkoder.spring.datajpa.response.BillManagement;

import com.bezkoder.spring.datajpa.model.BillManagement;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class GetAllBillManagement {
    private String status;
    private String message;
    
    private List<BillManagement> data;
}

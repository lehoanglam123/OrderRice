package com.bezkoder.spring.datajpa.response.BillManagement;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateBillManagementResponse {
	
    private String status;
    
    private String message;
}

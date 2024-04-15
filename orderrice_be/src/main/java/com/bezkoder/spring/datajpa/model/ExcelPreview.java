package com.bezkoder.spring.datajpa.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExcelPreview {
	private String status;
    private String message;
    
    private List<ExportExcelBill> listBill;
    
    private List<TotalPriceByAccount> listSumPriceByAccount;
}

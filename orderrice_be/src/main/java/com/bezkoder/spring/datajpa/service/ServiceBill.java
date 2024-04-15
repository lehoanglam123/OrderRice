package com.bezkoder.spring.datajpa.service;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.datajpa.model.BillManagement;
import com.bezkoder.spring.datajpa.model.ExcelPreview;
import com.bezkoder.spring.datajpa.request.ListBillManagement;
import com.bezkoder.spring.datajpa.response.BillManagement.CreateBillManagementResponse;
import com.bezkoder.spring.datajpa.response.BillManagement.DeleteBillManagementResponse;
import com.bezkoder.spring.datajpa.response.BillManagement.GetAllBillManagement;
import com.bezkoder.spring.datajpa.response.BillManagement.ListBillManagementResponse;
import com.bezkoder.spring.datajpa.response.BillManagement.UpdateBillManagementResponse;

@Service
public interface ServiceBill {
	
	public ResponseEntity<GetAllBillManagement> getAllBillManagement();
	
	public ResponseEntity<ListBillManagementResponse> getBillByIdEmployee(Integer idEmployee);
	
	//public ResponseEntity<JsonResponse> createBillManagement (BillManagement billManagement);
	ResponseEntity<CreateBillManagementResponse> createBillManagement(Integer idEmployee, BillManagement billManagement);
	
	public ResponseEntity<UpdateBillManagementResponse> updateBillManagementById(Integer id, BillManagement billManagement);

	public ResponseEntity<DeleteBillManagementResponse> deleteBillManagementById(ListBillManagement listid);

	void generateExcelBill(HttpServletResponse response, Date fromDate, Date toDate)throws Exception;

	public ResponseEntity<ExcelPreview> ExcelPreview(Date fromDate, Date toDate) throws Exception;

}

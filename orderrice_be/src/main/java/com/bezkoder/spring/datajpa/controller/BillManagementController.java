package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.model.BillManagement;
import com.bezkoder.spring.datajpa.model.ExcelPreview;
import com.bezkoder.spring.datajpa.request.ListBillManagement;
import com.bezkoder.spring.datajpa.response.BillManagement.*;
import com.bezkoder.spring.datajpa.service.ServiceBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bill")
@ResponseBody
public class BillManagementController {
	@Autowired
	private ServiceBill serviceBill;

	@GetMapping("/getall")
	public ResponseEntity<GetAllBillManagement> getAllBill() {
		return serviceBill.getAllBillManagement();
	}

	@GetMapping("/getAllByRangeDate")
	public ResponseEntity<ExcelPreview> ExcelPreview(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  Date fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception{
		return serviceBill.ExcelPreview(fromDate, toDate);
	}

	@GetMapping("/getById/{idEmployee}")
	public ResponseEntity<ListBillManagementResponse> getBillEmployeeId(@PathVariable Integer idEmployee) {
		return serviceBill.getBillByIdEmployee(idEmployee);
	}

	@PostMapping("/create/{idEmployee}")
	public ResponseEntity<CreateBillManagementResponse> createBill(@PathVariable Integer idEmployee,
			@RequestBody BillManagement billManagement) {
		return serviceBill.createBillManagement(idEmployee, billManagement);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<UpdateBillManagementResponse> updateBill(@PathVariable Integer id,
			@RequestBody BillManagement billManagement) {
		return serviceBill.updateBillManagementById(id, billManagement);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<DeleteBillManagementResponse> deleteBillManagementById(
			@RequestBody ListBillManagement Listid) {
		return serviceBill.deleteBillManagementById(Listid);
	}
}

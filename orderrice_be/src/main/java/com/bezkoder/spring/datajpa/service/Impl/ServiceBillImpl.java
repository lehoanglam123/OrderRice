package com.bezkoder.spring.datajpa.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.bezkoder.spring.datajpa.response.BillManagement.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.datajpa.model.BillManagement;
import com.bezkoder.spring.datajpa.model.BillManagementById;
import com.bezkoder.spring.datajpa.model.ExportExcelBill;
import com.bezkoder.spring.datajpa.model.TotalPriceByAccount;
import com.bezkoder.spring.datajpa.model.ExcelPreview;
import com.bezkoder.spring.datajpa.repository.BillRepository;
import com.bezkoder.spring.datajpa.request.ListBillManagement;
import com.bezkoder.spring.datajpa.service.ServiceBill;

@Service
public class ServiceBillImpl implements ServiceBill {

	@Autowired
	private BillRepository billRepository;

	@Override
	public ResponseEntity<GetAllBillManagement> getAllBillManagement() {
		List<BillManagement> listBill = billRepository.getAllBill();
		if (listBill.isEmpty()) {
			GetAllBillManagement listGetAllBillManagementResponseNoData = GetAllBillManagement.builder().status("204")
					.message("No content").build();
			return new ResponseEntity<GetAllBillManagement>(listGetAllBillManagementResponseNoData,
					HttpStatus.NO_CONTENT);
		}
		GetAllBillManagement listGetAllBillManagementResponseData = GetAllBillManagement.builder().data(listBill)
				.status("200").message("Successfully retrieved data").build();
		return new ResponseEntity<GetAllBillManagement>(listGetAllBillManagementResponseData, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ListBillManagementResponse> getBillByIdEmployee(Integer idEmployee) {
		List<BillManagementById> listBill = billRepository.getByIdEmployee(idEmployee);
		if (listBill.isEmpty()) {
			ListBillManagementResponse response = ListBillManagementResponse.builder().status("204")
					.message("No content").build();
			return new ResponseEntity<ListBillManagementResponse>(response, HttpStatus.NO_CONTENT);
		}
		ListBillManagementResponse response = ListBillManagementResponse.builder().status("200")
				.message("Get Successfully").data(listBill).build();
		return new ResponseEntity<ListBillManagementResponse>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CreateBillManagementResponse> createBillManagement(Integer idEmployee,
			BillManagement billManagement) {
		Optional<BillManagement> opbill = billRepository.getBillManagementByEmployeeIdAndCategoryIdAndPrice(idEmployee,
				billManagement.getIdCategory(), billManagement.getNote(), billManagement.getPrice());
		if (opbill.isPresent()) {
			opbill.get().setTotal(billManagement.getTotal() + opbill.get().getTotal());
			opbill.get().setPrice(billManagement.getPrice());
			billManagement.setId(opbill.get().getId());
			billRepository.updateQuantity(opbill.get());
			CreateBillManagementResponse Response = CreateBillManagementResponse.builder()
					.status("200")
					.message("Update Successfully")
					.build();
			return new ResponseEntity<CreateBillManagementResponse>(Response, HttpStatus.OK);
		}
		billManagement.setIdEmployee(idEmployee);
		billRepository.createBill(billManagement);
		CreateBillManagementResponse response = CreateBillManagementResponse.builder()
				.status("201")
				.message("Create Successfully")
				.build();
		return new ResponseEntity<CreateBillManagementResponse>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<UpdateBillManagementResponse> updateBillManagementById(Integer id,
			BillManagement billManagement) {
		BillManagement bill = billRepository.getBillManagementById(id);

		if (bill != null) {
			bill.setTotal(billManagement.getTotal());
			bill.setPrice(billManagement.getPrice());
			bill.setNote(billManagement.getNote());
			billRepository.updateBillManagementById(bill);
			UpdateBillManagementResponse response = UpdateBillManagementResponse.builder()
					.data(bill)
					.status("200")
					.message("Update Successfully")
					.build();
			return new ResponseEntity<UpdateBillManagementResponse>(response, HttpStatus.OK);
		}
		UpdateBillManagementResponse response = UpdateBillManagementResponse.builder().status("400")
				.message("Update failed").build();
		return new ResponseEntity<UpdateBillManagementResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<DeleteBillManagementResponse> deleteBillManagementById(ListBillManagement listid) {
		List<BillManagement> listBillById = billRepository.getListIdBillManagement(listid);
		if (listBillById.size() == listid.getIdBillManagement().size()) {
			billRepository.deleteListIdBillManagement(listid);
			DeleteBillManagementResponse response = DeleteBillManagementResponse.builder().status("200")
					.message("Delete Successfully!").build();
			return new ResponseEntity<DeleteBillManagementResponse>(response, HttpStatus.OK);
		}
		DeleteBillManagementResponse response = DeleteBillManagementResponse.builder().status("400")
				.message("There id does not exist in the database").build();
		return new ResponseEntity<DeleteBillManagementResponse>(response, HttpStatus.NOT_FOUND);
	}

	@Override
	public void generateExcelBill(HttpServletResponse response, Date start, Date end) throws Exception {
		List<ExportExcelBill> bill = billRepository.getBillByFromDateAndToDate(start, end);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Tất cả các đơn");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("STT");
		row.createCell(1).setCellValue("Tài khoản");
		row.createCell(2).setCellValue("Tên món ăn");
		row.createCell(3).setCellValue("Số lượng");
		row.createCell(4).setCellValue("Đơn giá");
		row.createCell(5).setCellValue("Ghi chú");
		row.createCell(6).setCellValue("Ngày đặt");

		int dataRowIndex = 1;
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(workbook.createDataFormat().getFormat("m/d/yy"));
		for (ExportExcelBill bills : bill) {

			HSSFRow dataRow = sheet.createRow(dataRowIndex);

			dataRow.createCell(0).setCellValue(dataRowIndex);
			dataRow.createCell(1).setCellValue(bills.getAccount());
			dataRow.createCell(2).setCellValue(bills.getFoodName());
			dataRow.createCell(3).setCellValue(bills.getTotal());
			dataRow.createCell(4).setCellValue(bills.getPrice());
			dataRow.createCell(5).setCellValue(bills.getNote());
			Cell dateCell = dataRow.createCell(6);
			dateCell.setCellStyle(cellStyle);
			dateCell.setCellValue(bills.getOrderDate());
			dataRowIndex++;
		}

		HSSFSheet sheetSum = workbook.createSheet("Thống kê theo tài khoản");
		HSSFRow rowSum = sheetSum.createRow(0);

		rowSum.createCell(0).setCellValue("STT");
		rowSum.createCell(1).setCellValue("Tài khoản");
		rowSum.createCell(2).setCellValue("Tổng số lượng");
		rowSum.createCell(3).setCellValue("Tổng tiền");

		int indexNew = 1;
		List<TotalPriceByAccount> totalPriceByAccount = billRepository
				.getSumTotalPriceByAccountByFromDateAndToDate(start, end);
		for (TotalPriceByAccount bills : totalPriceByAccount) {
			HSSFRow rowDataSum = sheetSum.createRow(indexNew);
			rowDataSum.createCell(0).setCellValue(indexNew);
			rowDataSum.createCell(1).setCellValue(bills.getAccount());
			rowDataSum.createCell(2).setCellValue(bills.getTotal());
			rowDataSum.createCell(3).setCellValue(bills.getPrice());
			indexNew++;
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

	@Override
	public ResponseEntity<ExcelPreview> ExcelPreview(Date start, Date end)
			throws Exception {
		List<ExportExcelBill> bill = billRepository.getBillByFromDateAndToDate(start, end);
		List<TotalPriceByAccount> totalPriceByAccount = billRepository
				.getSumTotalPriceByAccountByFromDateAndToDate(start, end);

		if (bill.isEmpty() || totalPriceByAccount.isEmpty()) {
			ExcelPreview respone = ExcelPreview.builder().status("204").message("Data is Empty").build();
			return new ResponseEntity<ExcelPreview>(respone, HttpStatus.NO_CONTENT);
		}
		ExcelPreview respone = ExcelPreview.builder().status("200").message("Get data Successfully").listBill(bill)
				.listSumPriceByAccount(totalPriceByAccount).build();
		return new ResponseEntity<ExcelPreview>(respone, HttpStatus.OK);
	}

}

package com.bezkoder.spring.datajpa.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.datajpa.model.BillManagementById;
import com.bezkoder.spring.datajpa.model.ExportExcelBill;
import com.bezkoder.spring.datajpa.model.TotalPriceByAccount;

import org.apache.ibatis.annotations.Mapper;

import com.bezkoder.spring.datajpa.model.BillManagement;
import com.bezkoder.spring.datajpa.request.ListBillManagement;

@Mapper
public interface BillRepository {

	public boolean createBill(BillManagement billManagement);

	public List<BillManagementById> getByIdEmployee(Integer idEmployee);

	public List<BillManagement> getAllBill();

	public BillManagement getBillManagementByEmployeeIdAndCategoryId(Integer idEmployee, Integer idCategory);

	public boolean updateQuantity(BillManagement billManagement);

	public boolean updateBillManagementById(BillManagement billManagement);

	public BillManagement getBillManagementById(Integer id);

	void deleteListIdBillManagement(ListBillManagement listId);

	List<BillManagement> getListIdBillManagement(ListBillManagement listId);

	public Optional<BillManagement> getBillManagementByEmployeeIdAndCategoryIdAndPrice(Integer idEmployee,
			Integer idCategory, String note, Integer price);

	public List<ExportExcelBill> getBillByFromDateAndToDate(Date fromDate, Date toDate);

	public List<TotalPriceByAccount> getSumTotalPriceByAccountByFromDateAndToDate(Date fromDate, Date toDate);

}

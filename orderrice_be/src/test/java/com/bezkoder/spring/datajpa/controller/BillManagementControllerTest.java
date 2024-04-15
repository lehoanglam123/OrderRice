 package com.bezkoder.spring.datajpa.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.bezkoder.spring.datajpa.model.BillManagement;
import com.bezkoder.spring.datajpa.model.BillManagementById;
import com.bezkoder.spring.datajpa.model.ExcelPreview;
import com.bezkoder.spring.datajpa.model.ExportExcelBill;
import com.bezkoder.spring.datajpa.model.TotalPriceByAccount;
import com.bezkoder.spring.datajpa.request.ListBillManagement;
import com.bezkoder.spring.datajpa.response.BillManagement.CreateBillManagementResponse;
import com.bezkoder.spring.datajpa.response.BillManagement.DeleteBillManagementResponse;
import com.bezkoder.spring.datajpa.response.BillManagement.GetAllBillManagement;
import com.bezkoder.spring.datajpa.response.BillManagement.ListBillManagementResponse;
import com.bezkoder.spring.datajpa.response.BillManagement.UpdateBillManagementResponse;
import com.bezkoder.spring.datajpa.service.Impl.ServiceBillImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BillManagementController.class)
class BillManagementControllerTest {
	final String pattern = "yyyy-MM-dd";
	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	private final static String URL_BILL_MANAGEMENT = "/api/bill/getById/id";
	private final static String URL_API_GETALL_BILL = "/api/bill/getall";
	private final static String URL_API_CREATE_BILL = "/api/bill/create/id";
	private final static String URL_API_UPDATE_BILL = "/api/bill/update/id";
	private final static String URL_API_DELETE_BILL = "/api/bill/delete";
	private final static String URL_API_GET_BILL_FROMEDATE_TODATE = "/api/bill/excel-preview";
	@Autowired
	private MockMvc mvc;

	List<BillManagementById> listBillManagement = new ArrayList<BillManagementById>();
	List<BillManagement> listBill = new ArrayList<BillManagement>();
	List<Integer> listId = new ArrayList<>();
	List<ExportExcelBill> listExportExcel = new ArrayList<ExportExcelBill>();
	List<TotalPriceByAccount> listTotal = new ArrayList<TotalPriceByAccount>();
	@MockBean
	private ServiceBillImpl serviceBill;
	@Captor
	private ArgumentCaptor<Integer> employeeIdCaptor;
	@Captor
	private ArgumentCaptor<BillManagement> billManagementCaptor;
	@Captor
	private ArgumentCaptor<Integer> billIdCaptor;
	@Captor
	private ArgumentCaptor<ListBillManagement> listBillByIdCaptor;
	@Captor
	private ArgumentCaptor<Date> fromDate, toDate;

	@BeforeEach
	void ListAllBill() {
		for (int i = 0; i < 3; i++) {
			BillManagement billManagement = BillManagement.builder().id(i).idEmployee(i).idCategory(i).total(5)
					.price(20000).note("Khong dau mo").build();
			listBill.add(billManagement);
		}
	}

	@Test
	void getAllBillManagement_normal01() throws Exception {
		GetAllBillManagement response = GetAllBillManagement.builder().status("200")
				.message("Successfully retrieved data").data(listBill).build();
		when(serviceBill.getAllBillManagement()).thenReturn(ResponseEntity.ok(response));

		mvc.perform(get(URL_API_GETALL_BILL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(jsonPath("$.message", is("Successfully retrieved data")))
				.andExpect(jsonPath("$.data", samePropertyValuesAs(response.getData())));

		verify(serviceBill, times(1)).getAllBillManagement();
		// assertThat()
	}

	@Test
	void getAllBillManagement_normal02() throws Exception {
		List<BillManagement> listBill = new ArrayList<BillManagement>();
		GetAllBillManagement response = GetAllBillManagement.builder().status("204").message("No content")
				.data(listBill).build();
		when(serviceBill.getAllBillManagement()).thenReturn(ResponseEntity.status(204).body(response));

		mvc.perform(get(URL_API_GETALL_BILL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is("204"))).andExpect(jsonPath("$.message", is("No content")))
				.andExpect(jsonPath("$.data", samePropertyValuesAs(response.getData())));

		verify(serviceBill, times(1)).getAllBillManagement();
		// assertThat()
	}

	@BeforeEach
	void initData() {
		final Integer idEmployee = 2;
		for (int i = 1; i < 3; i++) {
			BillManagementById billManagementById = BillManagementById.builder().id(1).idEmployee(idEmployee)
					.idCategory(i).price(20000).build();
			listBillManagement.add(billManagementById);
		}
	}

	@Test
	void getBillByEmployeeId_normal01() throws Exception {
		ListBillManagementResponse response = ListBillManagementResponse.builder().status("200")
				.message("Get Successfully").data(listBillManagement).build();

		when(serviceBill.getBillByIdEmployee(2)).thenReturn(ResponseEntity.ok().body(response));

		mvc.perform(get(URL_BILL_MANAGEMENT.replace("id", "2")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("200")))
				.andExpect(jsonPath("$.message", is("Get Successfully")))
				.andExpect(jsonPath("$.data", samePropertyValuesAs(response.getData())));

		// verify()

		verify(serviceBill, times(1)).getBillByIdEmployee(employeeIdCaptor.capture());
		assertThat(employeeIdCaptor.getValue(), is(2));
	}

	@Test
	void getBillByEmployeeId_normal02() throws Exception {
		List<BillManagementById> listBill = new ArrayList<BillManagementById>();
		ListBillManagementResponse response = ListBillManagementResponse.builder().status("204").message("No content")
				.data(listBill).build();

		when(serviceBill.getBillByIdEmployee(1)).thenReturn(ResponseEntity.status(204).body(response));

		mvc.perform(get(URL_BILL_MANAGEMENT.replace("id", "1")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andExpect(jsonPath("$.status", is("204")))
				.andExpect(jsonPath("$.message", is("No content")))
				.andExpect(jsonPath("$.data", samePropertyValuesAs(response.getData())));

		verify(serviceBill, times(1)).getBillByIdEmployee(employeeIdCaptor.capture());
		assertThat(employeeIdCaptor.getValue(), is(1));

	}

	@Test
	void createBillManagement_normal01() throws Exception {
		BillManagement billManagement = BillManagement.builder().id(1).idEmployee(1).idCategory(1).total(5).price(20000)
				.note("Khong dau mo").build();

		CreateBillManagementResponse response = CreateBillManagementResponse.builder().status("201")
				.message("Create Successfully").build();
		when(serviceBill.createBillManagement(1, billManagement)).thenReturn(ResponseEntity.status(201).body(response));

		mvc.perform(post(URL_API_CREATE_BILL.replace("id", "1"))
				.content(new ObjectMapper().writeValueAsString(billManagement)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.status", is("201")))
				.andExpect(jsonPath("$.message", is("Create Successfully")));

		verify(serviceBill, times(1)).createBillManagement(employeeIdCaptor.capture(), billManagementCaptor.capture());
		assertThat(employeeIdCaptor.getValue(), is(1));
		assertThat(billManagementCaptor.getValue(), is(billManagement));

	}

	@Test
	void createBillManagement_normal02() throws Exception {
		BillManagement billManagement = BillManagement.builder().id(1).idEmployee(1).idCategory(1).total(5).price(20000)
				.note("Khong dau mo").build();

		CreateBillManagementResponse response = CreateBillManagementResponse.builder().status("200")
				.message("Update Successfully").build();
		when(serviceBill.createBillManagement(1, billManagement)).thenReturn(ResponseEntity.ok().body(response));

		mvc.perform(post(URL_API_CREATE_BILL.replace("id", "1"))
				.content(new ObjectMapper().writeValueAsString(billManagement)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("200")))
				.andExpect(jsonPath("$.message", is("Update Successfully")));

		verify(serviceBill, times(1)).createBillManagement(employeeIdCaptor.capture(), billManagementCaptor.capture());
		assertThat(employeeIdCaptor.getValue(), is(1));
		assertThat(billManagementCaptor.getValue(), is(billManagement));
	}

	@Test
	void updateBillMagement_normal01() throws Exception {
		BillManagement billManagement = BillManagement.builder().id(1).idEmployee(2).idCategory(30).total(5)
				.price(20000).note("khong").build();

		UpdateBillManagementResponse response = UpdateBillManagementResponse.builder().data(billManagement)
				.status("200").message("Update Successfully").build();

		when(serviceBill.updateBillManagementById(1, billManagement)).thenReturn(ResponseEntity.ok(response));

		mvc.perform(post(URL_API_UPDATE_BILL.replace("id", "1"))
				.content(new ObjectMapper().writeValueAsString(billManagement)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("200")))
				.andExpect(jsonPath("$.message", is("Update Successfully")));
		// verify
		verify(serviceBill, times(1)).updateBillManagementById(billIdCaptor.capture(), billManagementCaptor.capture());
		assertThat(billIdCaptor.getValue(), is(1));
		assertThat(billManagementCaptor.getValue(), is(billManagement));
	}

	@Test
	void updateBillMagement_normal02() throws Exception {
		BillManagement billManagement = BillManagement.builder().id(1).idEmployee(2).idCategory(30).total(5)
				.price(20000).note("khong").build();

		UpdateBillManagementResponse response = UpdateBillManagementResponse.builder().data(billManagement)
				.status("404").message("Update failed").build();

		when(serviceBill.updateBillManagementById(2, billManagement)).thenReturn(ResponseEntity.ok(response));

		mvc.perform(post(URL_API_UPDATE_BILL.replace("id", "2"))
				.content(new ObjectMapper().writeValueAsString(billManagement)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("404")))
				.andExpect(jsonPath("$.message", is("Update failed")));
		// verify
		verify(serviceBill, times(1)).updateBillManagementById(billIdCaptor.capture(), billManagementCaptor.capture());
		assertThat(billIdCaptor.getValue(), is(2));
		assertThat(billManagementCaptor.getValue(), is(billManagement));
	}

	@Test
	void deleteBillManagementTest_normal01() throws Exception {
		List<Integer> listId = new ArrayList<Integer>();
		listId.add(1);
		listId.add(2);
		listId.add(3);
		DeleteBillManagementResponse response = DeleteBillManagementResponse.builder().status("200")
				.message("Delete successfully").build();
		ListBillManagement listBillById = new ListBillManagement();
		listBillById.setIdBillManagement(listId);

		when(serviceBill.deleteBillManagementById(listBillById)).thenReturn(ResponseEntity.status(200).body(response));

		mvc.perform(delete(URL_API_DELETE_BILL).content(new ObjectMapper().writeValueAsString(listBillById))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200));

		verify(serviceBill, times(1)).deleteBillManagementById(listBillByIdCaptor.capture());
		assertThat(listBillByIdCaptor.getValue().getIdBillManagement(),
				samePropertyValuesAs(listBillById.getIdBillManagement()));
	}
	
	@Test
	void deleteBillManagementTest_normal02() throws Exception {
		List<Integer> listId = new ArrayList<Integer>();
		listId.add(1);
		listId.add(2);
		listId.add(3);
		listId.add(4);
		DeleteBillManagementResponse response = DeleteBillManagementResponse.builder().status("400")
				.message("Delete successfully").build();
		ListBillManagement listBillById = new ListBillManagement();
		listBillById.setIdBillManagement(listId);

		when(serviceBill.deleteBillManagementById(any(ListBillManagement.class))).thenReturn(ResponseEntity.status(400).body(response));

		mvc.perform(delete(URL_API_DELETE_BILL).content(new ObjectMapper().writeValueAsString(listBillById))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(400));

		verify(serviceBill, times(1)).deleteBillManagementById(listBillByIdCaptor.capture());
		assertThat(listBillByIdCaptor.getValue().getIdBillManagement(),
				samePropertyValuesAs(listBillById.getIdBillManagement()));
	}


	@BeforeEach
	void listExportExcel() throws Exception {
		Long sum = 0L;
		for (int i = 1; i <= 3; i++) {
			ExportExcelBill billExport = ExportExcelBill.builder().id(i).account("LamLH" + i)
					.foodName("Cơm chiên dương châu" + i).price(i).total(20000L).note("Thập cẩm")
					.orderDate(simpleDateFormat.parse("2023-04-09")).build();
			listExportExcel.add(billExport);

			TotalPriceByAccount totalPriceByAccount = TotalPriceByAccount.builder().account("LamLH" + i).total(sum += i)
					.price(20000).build();
			listTotal.add(totalPriceByAccount);
		}
	}

	@Test
	void getBillByFromDateAndToDate_normal_01() throws Exception {
		ExcelPreview response = ExcelPreview.builder().status("200").message("Get data Successfully")
				.listBill(listExportExcel).listSumPriceByAccount(listTotal).build();

		when(serviceBill.ExcelPreview(simpleDateFormat.parse("2023-04-09"), simpleDateFormat.parse("2023-04-09")))
				.thenReturn(ResponseEntity.ok().body(response));

		mvc.perform(
				get(URL_API_GET_BILL_FROMEDATE_TODATE).param("fromDate", "2023-04-09").param("toDate", "2023-04-09"))
				.andExpect(status().isOk());

		verify(serviceBill, times(1)).ExcelPreview(fromDate.capture(), toDate.capture());
		assertThat(fromDate.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		assertThat(toDate.getValue(), is(simpleDateFormat.parse("2023-04-09")));
	}

	@Test
	void getBillByFromDateAndToDate_normal_02() throws Exception {
		ExcelPreview response = ExcelPreview.builder().status("400").message("Data is Empty").listBill(listExportExcel)
				.listSumPriceByAccount(listTotal).build();

		when(serviceBill.ExcelPreview(simpleDateFormat.parse("2023-04-10"), simpleDateFormat.parse("2023-04-09")))
				.thenReturn(ResponseEntity.status(400).body(response));

		mvc.perform(
				get(URL_API_GET_BILL_FROMEDATE_TODATE).param("fromDate", "2023-04-10").param("toDate", "2023-04-09"))
				.andExpect(status().isBadRequest());

		verify(serviceBill, times(1)).ExcelPreview(fromDate.capture(), toDate.capture());
		assertThat(fromDate.getValue(), is(simpleDateFormat.parse("2023-04-10")));
		assertThat(toDate.getValue(), is(simpleDateFormat.parse("2023-04-09")));
	}

//	@Test
//	void getBillByIdEmployee_normal02() throws Exception {
//		ListBillManagementResponse response = ListBillManagementResponse.builder().status("204")
//				.message("Get Not Found").build();
//
//		when(serviceBill.getBillByIdEmployee(2)).thenReturn(ResponseEntity.ok().body(response));
//
//		mvc.perform(
//				MockMvcRequestBuilders.get(URL_BillManagement + "/getById/2").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.status", is("204")))
//				.andExpect(jsonPath("$.message", is("Get Not Found")));
//
//		// verify()
//		verify(serviceBill, times(1)).getBillByIdEmployee(2);
//		assertThat(response.getStatus(), is("204"));
//		assertThat(response.getMessage(), is("Get Not Found"));
//	}
//
//	@Test
//	void updateBill_normal01() throws Exception {
//		BillManagement billManagement = BillManagement.builder().id(1).idEmployee(2).idCategory(30).total(5)
//				.price(20000).note("khong").build();
//
//		UpdateBillManagementResponse response = UpdateBillManagementResponse.builder().data(billManagement)
//				.status("200").message("Update Successfully").build();
//
//		when(serviceBill.updateBillManagementById(1, billManagement)).thenReturn(ResponseEntity.ok(response));
//
//		mvc.perform(MockMvcRequestBuilders.post(URL_BillManagement + "/update/1").content(asJsonString(billManagement))
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(jsonPath("$.status", is("200"))).andExpect(jsonPath("$.message", is("Update Successfully")));
//		// verify
//		verify(serviceBill, times(1)).updateBillManagementById(1, billManagement);
//		assertThat(response.getStatus(), is("200"));
//		assertThat(response.getMessage(), is("Update Successfully"));
//
//	}
//
//	@Test
//	void updateBill_normal02() throws Exception {
//		BillManagement billManagement = BillManagement.builder().id(1).idEmployee(2).idCategory(30).total(5)
//				.price(20000).note("khong").build();
//
//		UpdateBillManagementResponse response = UpdateBillManagementResponse.builder().status("404")
//				.message("Update failed!").build();
//
//		when(serviceBill.updateBillManagementById(2, billManagement)).thenReturn(ResponseEntity.ok(response));
//
//		mvc.perform(MockMvcRequestBuilders.post(URL_BillManagement + "/update/2").content(asJsonString(billManagement))
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(jsonPath("$.status", is("404"))).andExpect(jsonPath("$.message", is("Update failed!")));
//		// verify
//		verify(serviceBill, times(1)).updateBillManagementById(2, billManagement);
//		assertThat(response.getStatus(), is("404"));
//		assertThat(response.getMessage(), is("Update failed!"));
//
//	}
//
//	public static String asJsonString(final Object obj) {
//		try {
//			return new ObjectMapper().writeValueAsString(obj);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Test
//	void getAllBill_normal() throws Exception {
//		GetAllBillManagement response = GetAllBillManagement.builder().status("200")
//				.message("Successfully retrieved data!").build();
//
//		when(serviceBill.getAllBillManagement()).thenReturn(ResponseEntity.ok(response));
//
//		mvc.perform(MockMvcRequestBuilders.get(URL_API_GETALL).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.status", is("200")))
//				.andExpect(jsonPath("$.message", is("Successfully retrieved data!")));
//		// verify()
//		verify(serviceBill, times(1)).getAllBillManagement();
//		assertThat(response.getStatus(), is("200"));
//		assertThat(response.getMessage(), is("Successfully retrieved data!"));
//	}
//
//	@Test
//	void getAllBill_normal1() throws Exception {
//		GetAllBillManagement response = GetAllBillManagement.builder().status("204").message("No data!").build();
//
//		when(serviceBill.getAllBillManagement()).thenReturn(ResponseEntity.ok(response));
//
//		mvc.perform(MockMvcRequestBuilders.get(URL_API_GETALL).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.status", is("204")))
//				.andExpect(jsonPath("$.message", is("No data!")));
//		// verify()
//		verify(serviceBill, times(1)).getAllBillManagement();
//		assertThat(response.getStatus(), is("204"));
//		assertThat(response.getMessage(), is("No data!"));
//	}
//
//	@Test
//	void CreateBill_normal() throws Exception {
//		final String pattern = "yyyy-MM-dd";
//		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		CreateBillManagementResponse response = CreateBillManagementResponse.builder().status("200")
//				.message("Update Successfully").build();
//
//		BillManagement billManagement = BillManagement.builder().idEmployee(1).idCategory(1).total(2).price(20000)
//				.orderDate(simpleDateFormat.parse("2023-03-31")).note("abc").build();
//
//		when(serviceBill.createBillManagement(1, billManagement)).thenReturn(ResponseEntity.status(200).body(response));
//
//		mvc.perform(post(URL_API_CREATE).content(new ObjectMapper().writeValueAsString(billManagement))
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
//				.andExpect(jsonPath("$.status", is("200"))).andExpect(jsonPath("$.message", is("Update Successfully")));
//
//		// verify()
//		verify(serviceBill, times(1)).createBillManagement(1, billManagement);
//		assertThat(response.getStatus(), is("200"));
//		assertThat(response.getMessage(), is("Update Successfully"));
//	}
//
//	@Test
//	void CreateBill_normal1() throws Exception {
//		final String pattern = "yyyy-MM-dd";
//		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		CreateBillManagementResponse response = CreateBillManagementResponse.builder().status("200")
//				.message("Create Successfully").build();
//
//		BillManagement billManagement = BillManagement.builder().idEmployee(1).idCategory(1).total(2).price(20000)
//				.orderDate(simpleDateFormat.parse("2023-03-31")).note("abc").build();
//
//		when(serviceBill.createBillManagement(1, billManagement)).thenReturn(ResponseEntity.status(200).body(response));
//
//		mvc.perform(post(URL_API_CREATE).content(new ObjectMapper().writeValueAsString(billManagement))
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
//				.andExpect(jsonPath("$.status", is("200"))).andExpect(jsonPath("$.message", is("Create Successfully")));
//
//		// verify()
//		verify(serviceBill, times(1)).createBillManagement(1, billManagement);
//		assertThat(response.getStatus(), is("200"));
//		assertThat(response.getMessage(), is("Create Successfully"));
//	}
//
//	@Test
//	void deleteBill_normal() throws Exception {
//		List<Integer> listId = new ArrayList<>();
//		listId.add(33);
//		listId.add(34);
//		listId.add(35);
//		ListBillManagement lstBillId = new ListBillManagement();
//		lstBillId.setIdBillManagement(listId);
//		DeleteBillManagementResponse response = DeleteBillManagementResponse.builder().status("404")
//				.message("Delete Successfully!").build();
//		when(serviceBill.deleteBillManagementById(lstBillId)).thenReturn(ResponseEntity.ok().body(response));
//
//		mvc.perform(delete(URL_delete).content(new ObjectMapper().writeValueAsString(lstBillId))
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//	}

}
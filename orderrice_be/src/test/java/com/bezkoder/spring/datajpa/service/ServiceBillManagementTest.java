package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.model.*;
import com.bezkoder.spring.datajpa.repository.BillRepository;
import com.bezkoder.spring.datajpa.request.ListBillManagement;
import com.bezkoder.spring.datajpa.response.BillManagement.*;
import com.bezkoder.spring.datajpa.service.Impl.ServiceBillImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ServiceBillManagementTest {
	private static final int idEmployee = 1;
	  @InjectMocks
	  private ServiceBillImpl serviceBillImpl;
	  @Mock
	  private BillRepository billRepository;
	  @Captor
	  private ArgumentCaptor<Integer> employeeIdCaptor;
	  @Captor
	  private ArgumentCaptor<Integer> categoryIdCaptor;
	  @Captor
	  private ArgumentCaptor<String> billNoteCaptor;
	  @Captor
	  private ArgumentCaptor<Integer> captorId;
	  @Captor
	  private ArgumentCaptor<Integer> billPriceCaptor;
	  @Captor
	  private ArgumentCaptor<BillManagement> captorBillManagement;
	  @Captor
	  private ArgumentCaptor<ListBillManagement> captorListBillManagement;
	  @Captor
	  private ArgumentCaptor<Date> fromDateCaptor, toDateCaptor;

	  List<BillManagementById> listBill = new ArrayList<BillManagementById>();

	  List<BillManagement> bill = new ArrayList<>();
	  
	  List<ExportExcelBill> listGetByDate = new ArrayList<ExportExcelBill>();
	  List<TotalPriceByAccount> listTotalPriceByAccount = new ArrayList<TotalPriceByAccount>();
	  
	  final String pattern = "yyyy-MM-dd";
	  final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	  @BeforeEach
	  void initData() throws Exception {
	    final Integer employeeId = 2;
	    final String pattern = "yyyy-MM-dd";
	    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    Long s = 0L;
	    for (int i = 0; i < 3; i++) {
	      BillManagementById billManagement =
	          BillManagementById.builder().idEmployee(employeeId).idCategory(i).price(20000).build();
	      listBill.add(billManagement);

	      BillManagement billManagement1 =
	          BillManagement.builder().id(1).idEmployee(employeeId).idCategory(i).total(2).price(20000)
	              .orderDate(simpleDateFormat.parse("2023-03-31")).note("abc").build();
	      bill.add(billManagement1);
	      
	      ExportExcelBill billExport = ExportExcelBill.builder().id(i).account("LamLH" + i)
					.foodName("Cơm chiên dương châu" + i).price(i).total(20000L).note("Thập cẩm")
					.orderDate(simpleDateFormat.parse("2023-04-09")).build();
		  listGetByDate.add(billExport);

		  TotalPriceByAccount totalPriceByAccount = TotalPriceByAccount.builder().account("LamLH" + i).total(i+s)
					.price(20000).build();
		  listTotalPriceByAccount.add(totalPriceByAccount);
	    }
	  }

	  @Test
	  void getByIdBillManagementTest_normal_01() throws Exception {
	    when(billRepository.getByIdEmployee(2)).thenReturn(listBill);

	    ResponseEntity<ListBillManagementResponse> listBillManagement =
	        serviceBillImpl.getBillByIdEmployee(2);
	    verify(billRepository, times(1)).getByIdEmployee(employeeIdCaptor.capture());

	    assertThat(employeeIdCaptor.getValue(), is(2));
	    assertThat(listBillManagement.getBody().getStatus(), is("200"));
	    assertThat(listBillManagement.getBody().getData(), samePropertyValuesAs(listBill));
	  }

	  @Test
	  void getByIdBillManagementTest_normal_02() throws Exception {
	    List<BillManagementById> listBill = new ArrayList<BillManagementById>();
	    //listBill = null;
	    when(billRepository.getByIdEmployee(1)).thenReturn(listBill);

	    ResponseEntity<ListBillManagementResponse> responseEntity =
	        serviceBillImpl.getBillByIdEmployee(1);

	    verify(billRepository, times(1)).getByIdEmployee(employeeIdCaptor.capture());

	    assertThat(employeeIdCaptor.getValue(), is(1));
	    assertThat(responseEntity.getBody().getStatus(), is("204"));
	    //assertThat(listBillManagement.getBody().getData(), samePropertyValuesAs(listBill));
	  }

	  @Test
	  void updateBillManagementById_normal_01() throws Exception {
	    BillManagement billManagement = BillManagement.builder().id(1).idEmployee(2).idCategory(2)
	        .total(2).price(20000).note("Khong ca").build();

	    when(billRepository.getBillManagementById(1)).thenReturn(billManagement);
	    when(billRepository.updateBillManagementById(billManagement)).thenReturn(true);

	    ResponseEntity<UpdateBillManagementResponse> responseEntity =
	        serviceBillImpl.updateBillManagementById(1, billManagement);

	    verify(billRepository, times(1)).getBillManagementById(captorId.capture());
	    verify(billRepository, times(1)).updateBillManagementById(captorBillManagement.capture());

	    assertThat(captorId.getValue(), is(1));
	    assertThat(captorBillManagement.getValue(), samePropertyValuesAs(billManagement));
	    assertThat(responseEntity.getBody().getStatus(), is("200"));
	    assertThat(responseEntity.getBody().getData(), samePropertyValuesAs(billManagement));
	  }

	  @Test
	  void updateBillManagementById_normal_02() throws Exception {
	    BillManagement billManagement = BillManagement.builder().id(1).idEmployee(2).idCategory(2)
	        .total(2).price(20000).note("Khong ca").build();

	    when(billRepository.getBillManagementById(2)).thenReturn(null);
	    ResponseEntity<UpdateBillManagementResponse> responseEntity =
	        serviceBillImpl.updateBillManagementById(2, billManagement);
	    verify(billRepository, times(1)).getBillManagementById(captorId.capture());

	    assertThat(captorId.getValue(), is(2));
	    assertThat(responseEntity.getBody().getStatus(), is("404"));
	  }

	  @Test
	  public void testGetAllBill_normal_01() throws Exception {
	    List<BillManagement> listBill = bill;
	    when(billRepository.getAllBill()).thenReturn(listBill);
	    ResponseEntity<GetAllBillManagement> responseEntity = serviceBillImpl.getAllBillManagement();
	    verify(billRepository, times(1)).getAllBill();
	    assertThat(responseEntity.getBody().getStatus(), is("200"));
	    assertThat(responseEntity.getBody().getData(), samePropertyValuesAs(listBill));
	  }

	  @Test
	  public void testGetAllBill_normal_02() throws Exception {
	    List<BillManagement> listBillEmpty = new ArrayList<>();
	    when(billRepository.getAllBill()).thenReturn(listBillEmpty);
	    ResponseEntity<GetAllBillManagement> responseEntity = serviceBillImpl.getAllBillManagement();

	    verify(billRepository, times(1)).getAllBill();
	    assertThat(responseEntity.getBody().getStatus(), is("204"));
	  }

	  @Test
	  public void testCreateBill_normal_01() throws Exception {
	    final String pattern = "yyyy-MM-dd";
	    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    BillManagement billManagement = null;
	    for (int i = 0; i < 3; i++) {
	      billManagement = BillManagement.builder().id(1).idEmployee(1).idCategory(1).total(2)
	          .price(20000).orderDate(simpleDateFormat.parse("2023-03-31")).note("abc").build();
	    }
	    Optional<BillManagement> optional = Optional.empty();

	    when(billRepository.getBillManagementByEmployeeIdAndCategoryIdAndPrice(idEmployee,
	        billManagement.getIdCategory(), billManagement.getNote(), billManagement.getPrice()))
	            .thenReturn(optional);
	    when(billRepository.createBill(billManagement)).thenReturn(true);

	    ResponseEntity<CreateBillManagementResponse> createBillManagementResponseResponseEntity =
	        serviceBillImpl.createBillManagement(idEmployee, billManagement);

	    verify(billRepository, times(1)).createBill(captorBillManagement.capture());
	    assertThat(createBillManagementResponseResponseEntity.getBody().getStatus(), is("201"));
	  }

	  @Test
	  public void testCreateBill_normal_02() throws Exception {
	    final String pattern = "yyyy-MM-dd";
	    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    BillManagement billManagement = BillManagement.builder().id(1).idEmployee(1).idCategory(1)
	        .total(2).price(20000).orderDate(simpleDateFormat.parse("2023-03-31")).note("abc").build();
	    Optional<BillManagement> optional = Optional.ofNullable(billManagement);

	    when(billRepository.getBillManagementByEmployeeIdAndCategoryIdAndPrice(idEmployee,
	        billManagement.getIdCategory(), billManagement.getNote(), billManagement.getPrice()))
	            .thenReturn(optional);
	    when(billRepository.updateQuantity(billManagement)).thenReturn(true);
	    ResponseEntity<CreateBillManagementResponse> createBillManagementResponseResponseEntity1 =
	        serviceBillImpl.createBillManagement(idEmployee, billManagement);

	    verify(billRepository, times(1)).updateQuantity(captorBillManagement.capture());
	    assertThat(captorBillManagement.getValue(), is(billManagement));
	    assertThat(createBillManagementResponseResponseEntity1.getBody().getStatus(), is("200"));

	  }

	  @Test
	  public void testDeleteBill_normal_01() throws Exception {
	    List<Integer> listId = new ArrayList<>();
	    listId.add(33);
	    listId.add(34);
	    listId.add(35);
	    ListBillManagement lstBillId = new ListBillManagement();
	    lstBillId.setIdBillManagement(listId);
	    when(billRepository.getListIdBillManagement(lstBillId)).thenReturn(bill);
	    ResponseEntity<DeleteBillManagementResponse> createBillManagementResponseResponseEntity =
	        serviceBillImpl.deleteBillManagementById(lstBillId);

	    verify(billRepository, times(1)).getListIdBillManagement(captorListBillManagement.capture());
	    assertThat(captorListBillManagement.getValue().getIdBillManagement(), is(listId));
	    assertThat(createBillManagementResponseResponseEntity.getBody().getStatus(), is("200"));
	  }

	  @Test
	  public void testDeleteBill_normal_02() throws Exception {
	    List<Integer> listId = new ArrayList<>();
	    listId.add(33);
	    listId.add(34);
	    listId.add(35);
	    ListBillManagement lstBillId = new ListBillManagement();
	    lstBillId.setIdBillManagement(listId);
	    ResponseEntity<DeleteBillManagementResponse> responseEntity =
	        serviceBillImpl.deleteBillManagementById(lstBillId);

	    verify(billRepository, times(1)).getListIdBillManagement(captorListBillManagement.capture());
	    assertThat(captorListBillManagement.getValue().getIdBillManagement(), is(listId));
	    assertThat(responseEntity.getBody().getStatus(), is("400"));
	  }
	  
	  
	  @Test
	  void excelPreviewTest_normal_01() throws Exception{
		  Date start = simpleDateFormat.parse("2023-04-09");
		  Date end = simpleDateFormat.parse("2023-04-09");
		  //when
		  when(billRepository.getBillByFromDateAndToDate(start, end))
		  .thenReturn(listGetByDate);
		  when(billRepository.getSumTotalPriceByAccountByFromDateAndToDate(start, end))
		  .thenReturn(listTotalPriceByAccount);
		  
		  ResponseEntity<ExcelPreview> responseEntity =
				  serviceBillImpl.ExcelPreview(start, end);
		  //verify
		  verify(billRepository, times(1)).getBillByFromDateAndToDate(fromDateCaptor.capture(), toDateCaptor.capture());
		  assertThat(fromDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(toDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));		  
		  verify(billRepository, times(1)).getSumTotalPriceByAccountByFromDateAndToDate(fromDateCaptor.capture(), toDateCaptor.capture());
		  assertThat(fromDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(toDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(responseEntity.getBody().getStatus(), is("200"));
		  assertThat(responseEntity.getBody().getListBill(), samePropertyValuesAs(listGetByDate));
		  assertThat(responseEntity.getBody().getListSumPriceByAccount(), samePropertyValuesAs(listTotalPriceByAccount));
		  
	  }
	  @Test
	  void excelPreviewTest_normal_02() throws Exception{
		  List<ExportExcelBill> listGet = new ArrayList<ExportExcelBill>();		  
		  List<TotalPriceByAccount> listTotal = new ArrayList<TotalPriceByAccount>();
		  Date start = simpleDateFormat.parse("2023-04-09");
		  Date end = simpleDateFormat.parse("2023-04-09");
		  //when
		  when(billRepository.getBillByFromDateAndToDate(start, end)).thenReturn(listGet);
		  when(billRepository.getSumTotalPriceByAccountByFromDateAndToDate(start, end)).thenReturn(listTotal);
		  //create ResponseEntity
		  ResponseEntity<ExcelPreview> responseEntity =
				  serviceBillImpl.ExcelPreview(start, end);
		  
		  verify(billRepository, times(1)).getBillByFromDateAndToDate(fromDateCaptor.capture(), toDateCaptor.capture());
		  verify(billRepository, times(1)).getSumTotalPriceByAccountByFromDateAndToDate(fromDateCaptor.capture(), toDateCaptor.capture());
		  
		  assertThat(fromDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(toDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(fromDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(toDateCaptor.getValue(), is(simpleDateFormat.parse("2023-04-09")));
		  assertThat(responseEntity.getBody().getStatus(), is("204"));
		  
	  }
//	@Test
//	void getByIdBillManagementTest_normal_01() throws Exception{
//		ListBillManagementResponse response = ListBillManagementResponse.builder()
//				.status("200")
//				.message("Get Successfully")
//				.data(listBill)
//				.build();
//		when(billRepository.getByIdEmployee(2)).thenReturn(listBill);
//		
//		ResponseEntity<ListBillManagementResponse> listBillManagement = serviceBillImpl.getBillByIdEmployee(2);
//		
//		assertEquals(HttpStatus.OK, listBillManagement.getStatusCode());
//		assertEquals(response.getData(), listBillManagement.getBody().getData());
//	}
//	
//	@Test
//	void getByIdBillManagementTest_normal_02() throws Exception{
//		List<BillManagementById> listBill = new ArrayList<BillManagementById>();
//		ListBillManagementResponse response = ListBillManagementResponse.builder()
//				.status("204")
//				.message("No conten")
//				.build();
//		when(billRepository.getByIdEmployee(1)).thenReturn(listBill);
//		
//		ResponseEntity<ListBillManagementResponse> listBillManagement = serviceBillImpl.getBillByIdEmployee(1);
//		
//		assertEquals(HttpStatus.NO_CONTENT, listBillManagement.getStatusCode());
//		assertEquals(response.getData(), listBillManagement.getBody().getData());
//	}
//	
//	@Test
//	void updateBillManagementById_normal_01() throws Exception{
//		BillManagement billManagement = BillManagement.builder()
//				.id(1)
//				.idEmployee(2)
//				.idCategory(2)
//				.total(2)
//				.price(20000)
//				.note("Khong ca")
//				.build();
//		
//		UpdateBillManagementResponse response = UpdateBillManagementResponse.builder()
//				.status("200")
//				.message("Update Successfully")
//				.data(billManagement)
//				.build();
//		 
//		when(billRepository.getBillManagementById(1)).thenReturn(billManagement);
//		when(billRepository.updateBillManagementById(billManagement)).thenReturn(true);
//		
//		ResponseEntity<UpdateBillManagementResponse> updateBillManagementResponse = serviceBillImpl.updateBillManagementById(1, billManagement);
//		
//		assertEquals(HttpStatus.OK, updateBillManagementResponse.getStatusCode());
//		assertEquals(response.getData(), updateBillManagementResponse.getBody().getData());
//	}
//	
//	@Test
//	void updateBillManagementById_normal_02() throws Exception{
//		BillManagement billManagement = BillManagement.builder()
//				.id(1)
//				.idEmployee(2)
//				.idCategory(2)
//				.total(2)
//				.price(20000)
//				.note("Khong ca")
//				.build();
//		 
//		when(billRepository.getBillManagementById(2)).thenReturn(null);
//		ResponseEntity<UpdateBillManagementResponse> updateBillManagementResponse = serviceBillImpl.updateBillManagementById(2, billManagement);
//		
//		assertEquals(HttpStatus.NOT_FOUND, updateBillManagementResponse.getStatusCode());
//	}
//
//	@Test
//	public void testGetAllBill() throws Exception{
//		List<BillManagement> listBill = bill;
//
//		CreateBillManagement createBillManagement = CreateBillManagement.builder().data(listBill).status("200").message("Successfully retrieved data!").build();
//		when(billRepository.getAllBill()).thenReturn(listBill);
//		ResponseEntity<CreateBillManagement> listBillResponse = serviceBillImpl.getAllBillManagement();
//
//		assertEquals(createBillManagement.getData(), listBillResponse.getBody().getData());
//		assertEquals(HttpStatus.OK, listBillResponse.getStatusCode());
//	}
//
//	@Test
//	public void testGetAllBill1() throws Exception{
//		List<BillManagement> listBillEmpty = new ArrayList<>();
//
//		CreateBillManagement createBillManagement = CreateBillManagement.builder().status("204").message("data!").build();
//		when(billRepository.getAllBill()).thenReturn(listBillEmpty);
//		ResponseEntity<CreateBillManagement> listBillResponse = serviceBillImpl.getAllBillManagement();
//
//		assertEquals(createBillManagement.getData(), listBillResponse.getBody().getData());
//		assertEquals(HttpStatus.NO_CONTENT, listBillResponse.getStatusCode());
//	}
//
//	@Test
//	public void testCreateBill() throws Exception {
//		final String pattern = "yyyy-MM-dd";
//		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		for (int i = 0; i < 3; i++) {
//			BillManagement billManagement = BillManagement.builder()
//					.id(1)
//					.idEmployee(1)
//					.idCategory(1)
//					.total(2)
//					.price(20000)
//					.orderDate(simpleDateFormat.parse("2023-03-31"))
//					.note("abc")
//					.build();
//
//			Optional<BillManagement> optional = Optional.empty();
//
//			when(billRepository.getBillManagementByEmployeeIdAndCategoryIdAndPrice(idEmployee, billManagement.getIdCategory(), billManagement.getNote(), billManagement.getPrice())).thenReturn(optional);
//			when(billRepository.createBill(billManagement)).thenReturn(true);
//			ResponseEntity<CreateBillManagementResponse> createBillManagementResponseResponseEntity = serviceBillImpl.createBillManagement(idEmployee, billManagement);
//
//			assertEquals(HttpStatus.CREATED, createBillManagementResponseResponseEntity.getStatusCode());
//		}
//	}
//
//	@Test
//	public void testCreateBill1() throws Exception {
//		final String pattern = "yyyy-MM-dd";
//		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		BillManagement billManagement = BillManagement.builder()
//				.id(1)
//				.idEmployee(1)
//				.idCategory(1)
//				.total(2)
//				.price(20000)
//				.orderDate(simpleDateFormat.parse("2023-03-31"))
//				.note("abc")
//				.build();
//		Optional<BillManagement> optional = Optional.ofNullable(billManagement);
//
//		when(billRepository.getBillManagementByEmployeeIdAndCategoryIdAndPrice(idEmployee, billManagement.getIdCategory(), billManagement.getNote(), billManagement.getPrice())).thenReturn(optional);
//		when(billRepository.updateQuantity(billManagement)).thenReturn(true);
//		ResponseEntity<CreateBillManagementResponse> createBillManagementResponseResponseEntity1 = serviceBillImpl.createBillManagement(idEmployee, billManagement);
//
//		assertEquals(HttpStatus.OK, createBillManagementResponseResponseEntity1.getStatusCode());
//
//	}
//
//	@Test
//	public void testDeleteBill() throws Exception {
//		List<Integer> listId = new ArrayList<>();
//		listId.add(33);
//		listId.add(34);
//		listId.add(35);
//		ListBillManagement lstBillId = new ListBillManagement();
//		lstBillId.setIdBillManagement(listId);
//		when(billRepository.getListIdBillManagement(lstBillId)).thenReturn(bill);
//		ResponseEntity<DeleteBillManagementResponse> createBillManagementResponseResponseEntity = serviceBillImpl.deleteBillManagementById(lstBillId);
//
//		assertEquals(HttpStatus.OK, createBillManagementResponseResponseEntity.getStatusCode());
//	}
//
//	@Test
//	public void testDeleteBill1() throws Exception {
//		List<Integer> listId = new ArrayList<>();
//		listId.add(33);
//		listId.add(34);
//		listId.add(35);
//		ListBillManagement lstBillId = new ListBillManagement();
//		lstBillId.setIdBillManagement(listId);
//		ResponseEntity<DeleteBillManagementResponse> createBillManagementResponseResponseEntity = serviceBillImpl.deleteBillManagementById(lstBillId);
//
//		assertEquals(HttpStatus.NOT_FOUND, createBillManagementResponseResponseEntity.getStatusCode());
//	}
}

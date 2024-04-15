package com.bezkoder.spring.datajpa.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class BillManagement implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer idEmployee;
	private Integer idCategory;
	private Integer total;
	private Integer price;
	private Date orderDate;
	private String note;

}

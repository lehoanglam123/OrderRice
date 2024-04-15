package com.bezkoder.spring.datajpa.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryLimit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String foodName;
	private Long price;
	private Date createDate;
	
}

package com.bezkoder.spring.datajpa.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String foodName;
	private Date createDate;
	private Long price;
	private Integer active;
}

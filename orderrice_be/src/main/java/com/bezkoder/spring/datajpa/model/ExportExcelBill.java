package com.bezkoder.spring.datajpa.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@AllArgsConstructor
public class ExportExcelBill implements Serializable{
        private static final long serialVersionUID = 1L;
        @Id
        private Integer id;

        private String account;

        private String foodName ;

        private Integer price;

        private Long total;
        
        private String note;

        @Temporal(TemporalType.DATE)
        private Date orderDate;
        
        
    }


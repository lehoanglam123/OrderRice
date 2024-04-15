package com.bezkoder.spring.datajpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TotalPriceByAccount {

    private String account;

    private Long total;

    private Integer price;

}

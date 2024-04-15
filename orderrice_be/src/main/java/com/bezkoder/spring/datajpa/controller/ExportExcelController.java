package com.bezkoder.spring.datajpa.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.service.ServiceBill;


@RestController
@RequestMapping("/api/export")
@ResponseBody
@CrossOrigin(origins = "*")
public class ExportExcelController {
    @Autowired
    private ServiceBill serviceBill;

    @GetMapping("/excel")
    public void generateExcelBill(HttpServletResponse response, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  Date fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws Exception
    {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=bill"+currentDateTime+".xls";

        response.setHeader(headerKey, headerValue);

        serviceBill.generateExcelBill(response, fromDate, toDate);
    }
}
package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.annual.AnnualReportExpenseBody;
import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnualReportService {
    List<AnnualReport> getListAnnualReportPaging(Pageable pageable);

    long countDistinctListAnnualReportPaging();

    List<Report> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable);

    long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId);
}
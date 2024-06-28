package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialReport;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialReportService {
    List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Pageable pageable) throws Exception;

    long countDistinctListReportPaginate(String query, Long termId, Long departmentId, Long statusId) throws Exception;
}

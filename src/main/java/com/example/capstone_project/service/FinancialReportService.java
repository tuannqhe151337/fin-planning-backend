package com.example.capstone_project.service;

import com.example.capstone_project.controller.responses.report.approval.ExpenseCodeResponse;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.ReportStatus;
import com.example.capstone_project.repository.result.*;
import com.example.capstone_project.service.result.CostResult;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public interface FinancialReportService {
    List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Integer pageInt, Integer sizeInt, String sortBy, String sortType) throws Exception;

    long countDistinctListReportPaginate(String query, Long termId, Long statusId) throws Exception;

//    long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId);

    ReportDetailResult getReportDetailByReportId(Long reportId) throws Exception;

    FinancialReport deleteReport(Long reportId);

    byte[] getBodyFileExcelXLSX(Long reportId) throws Exception;

    String generateXLSXFileName(Long reportId);

    byte[] getBodyFileExcelXLS(Long reportId) throws Exception;

    String generateXLSFileName(Long reportId);

    List<ReportExpenseResult> getListExpenseWithPaginate(Long reportId, String query, Integer departmentId, Integer statusId, Integer costTypeId, Integer projectId, Integer supplierId, Integer picId, Long currencyId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer departmentId, Integer statusId, Integer costTypeId, Integer projectId, Integer supplierId, Integer picId);

    CostResult calculateActualCostByReportId(Long reportId) throws Exception;

    CostResult calculateExpectedCostByReportId(Long reportId) throws Exception;

    List<ExpenseCodeResponse> approvalExpenses(Long planId, List<Long> listExpenses) throws Exception;

    void denyExpenses(Long planId, List<Long> listExpenseId) throws Exception;

    void approvalAllExpenses(Long reportId) throws Exception;

    void uploadReportExpenses(Long reportId, List<FinancialPlanExpense> rawExpenses) throws Exception;

    List<YearDiagramResult> generateYearDiagram(Integer year) throws Exception;

    List<CostTypeDiagramResult> getYearCostTypeDiagram(Integer year) throws Exception;

    List<DepartmentDiagramResult> getYearDepartmentDiagram(Integer year) throws Exception;

    TreeMap<String, List<CostTypeDiagramResult>> getReportCostTypeDiagram(Integer year) throws Exception;

    void markReportAsReviewed(Long reportId) throws Exception;

    List<ReportStatus> getListReportStatus();
}

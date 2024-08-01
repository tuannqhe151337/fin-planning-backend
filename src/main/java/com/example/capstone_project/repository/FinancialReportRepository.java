package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.repository.result.ReportDetailResult;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long>, CustomFinancialReportRepository {
    @Query(value = " SELECT DISTINCT count(report.id) FROM FinancialReport report " +
            " WHERE report.name like %:query% AND " +
            " (:termId IS NULL OR report.term.id = :termId) AND " +
            " (:statusId IS NULL OR report.status.id = :statusId) AND " +
            " report.isDelete = false ")
    long countDistinctListReportPaginate(@Param("query") String query, @Param("termId") Long termId, @Param("statusId") Long statusId);

//    @Query(" SELECT report.id AS reportId, report.name AS name, MAX(expense.unitPrice * expense.amount) AS biggestExpenditure, " +
//            " SUM(expense.unitPrice * expense.amount) AS totalCost, term.id AS termId, term.name AS termName, " +
//            " term.planDueDate AS planDueDate, report.createdAt AS createdAt, department.id AS departmentId, department.name AS departmentName, " +
//            " user.id AS userId , user.username AS username" +
//            " FROM FinancialReport report " +
//            " JOIN report.term term " +
//            " JOIN report.department department " +
//            " JOIN report.reportExpenses expense " +
//            " JOIN report.user user" +
//            " WHERE report.id = :reportId AND " +
//            " report.isDelete = false AND expense.isDelete = false " +
//            " GROUP BY report.id, report.name, term.id, term.name, term.planDueDate, " +
//            " report.createdAt, department.id, department.name, user.id, user.username ")
//    ReportDetailResult getFinancialReportById(Long reportId);

    @Query(value = " SELECT term.name AS termName FROM FinancialReport report " +
            " JOIN report.term term " +
            " WHERE report.id = :reportId AND " +
            " report.isDelete = false AND term.isDelete = false ")
    FileNameResult generateFileName(Long reportId);

    @Query(" SELECT SUM(expense.amount*expense.unitPrice) FROM FinancialPlanExpense expense " +
            " LEFT JOIN expense.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " LEFT JOIN plan.department department " +
            " LEFT JOIN expense.status status " +
            " LEFT JOIN expense.costType costType " +
            " WHERE file.id IN (SELECT MAX(file_2.id) FROM FinancialPlanFile file_2 " +
            "                       JOIN file_2.plan plan_2 " +
            "                       JOIN plan_2.term term_2 " +
            "                       JOIN term_2.financialReports report_2 " +
            "                       WHERE report_2.id = :reportId) AND " +
            " expense.status.code = :approved AND " +
            " (expense.isDelete = false OR expense.isDelete is null) ")
    BigDecimal calculateActualCostByReportId(Long reportId, ExpenseStatusCode approved);

    @Query(" SELECT SUM(expense.amount*expense.unitPrice) FROM FinancialPlanExpense expense " +
            " LEFT JOIN expense.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " LEFT JOIN plan.department department " +
            " LEFT JOIN expense.status status " +
            " LEFT JOIN expense.costType costType " +
            " WHERE file.id IN (SELECT MAX(file_2.id) FROM FinancialPlanFile file_2 " +
            "                       JOIN file_2.plan plan_2 " +
            "                       JOIN plan_2.term term_2 " +
            "                       JOIN term_2.financialReports report_2 " +
            "                       WHERE report_2.id = :reportId) AND " +
            " (expense.isDelete = false OR expense.isDelete is null) ")
    BigDecimal calculateExpectedCostByReportId(Long reportId);

//    @Query(value = " SELECT expenses FROM FinancialReportExpense expenses " +
//            " JOIN expenses.financialReport report " +
//            " WHERE report.id = :reportId AND " +
//            " expenses.isDelete = false AND report.isDelete = false ")
//    List<ExpenseResult> getListExpenseByReportId(Long reportId);

//    @Query(value = " SELECT report.department.id FROM FinancialReport report " +
//            " WHERE report.id = :reportId AND " +
//            " report.isDelete = false ")
//    long getDepartmentId(Long reportId);
}
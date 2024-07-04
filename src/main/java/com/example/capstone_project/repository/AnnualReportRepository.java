package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.ExpenseStatus;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AnnualReportRepository extends JpaRepository<AnnualReport, Long>, CustomAnnualReportRepository {
    @Query(value = "SELECT count( distinct(annualReport)) FROM AnnualReport annualReport " +
            " WHERE annualReport.isDelete = false ")
    long countDistinctListAnnualReportPaging();

    @Query(value = " SELECT year (term.planDueDate) AS year, count (term.id) AS totalTerm, " +
            " sum (expense.amount*expense.unitPrice) AS TotalExpense, count (department.id)  FROM Term term " +
            " JOIN term.financialReports report" +
            " JOIN report.department department " +
            " JOIN report.reportExpenses expense " +
            " WHERE year (term.planDueDate) = year (:now) AND " +
            " expense.status.name = :approved AND" +
            " term.isDelete = false AND " +
            " expense.isDelete = false AND " +
            " department.isDelete = false " +
            " GROUP BY year ")
    AnnualReportResult getAnnualReport(LocalDate now, ExpenseStatusCode approved);

    @Query(value = " SELECT report.department.id AS departmentId, sum(expense.unitPrice*expense.amount) AS totalExpense, max(expense.unitPrice*expense.amount) AS biggestExpense, costType.id AS costTypeId FROM FinancialReportExpense expense " +
            " JOIN expense.costType costType " +
            " JOIN expense.financialReport report " +
            " JOIN report.department department " +
            " JOIN report.term term " +
            " WHERE year(term.planDueDate) = year(:now) AND " +
            " expense.status = :approved AND" +
            " term.isDelete = false AND " +
            " report.isDelete = false AND " +
            " department.isDelete = false AND " +
            " costType.isDelete = false AND " +
            " expense.isDelete = false " +
            " GROUP BY departmentId, costTypeId")
    List<ReportResult> getListReports(LocalDate now, ExpenseStatusCode approved);
}

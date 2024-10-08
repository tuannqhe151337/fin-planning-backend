package com.example.capstone_project.repository;

import com.example.capstone_project.entity.MonthlyReportSummary;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import com.example.capstone_project.repository.result.DepartmentDiagramResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonthlyReportSummaryRepository extends JpaRepository<MonthlyReportSummary, Long> {
    @Query(value = " SELECT report.costType.id AS costTypeId, report.costType.name AS costTypeName, sum(report.totalExpense) AS totalCost FROM AnnualReport annualReport " +
            " JOIN annualReport.monthlyReportSummaries report " +
            " WHERE annualReport.year = :year AND " +
            " (report.department.id = :departmentId OR :departmentId is null) AND " +
            " annualReport.isDelete = false AND report.isDelete = false " +
            " GROUP BY costTypeId, costTypeName " +
            " ORDER BY totalCost desc")
    List<CostTypeDiagramResult> getCostTypeYearDiagram(Integer year, Long departmentId);

    @Query(value = " SELECT reportStatistic.costType.id AS costTypeId, reportStatistic.costType.name AS costTypeName, sum(reportStatistic.totalExpense) AS totalCost FROM ReportStatistical reportStatistic " +
            " JOIN reportStatistic.report report " +
            " JOIN report.term term " +
            " WHERE year(term.finalEndTermDate) = :year AND " +
            " (reportStatistic.department.id = :departmentId OR :departmentId is null) AND " +
            " report.isDelete = false AND term.isDelete = false " +
            " GROUP BY costTypeId, costTypeName " +
            " ORDER BY totalCost desc")
    List<CostTypeDiagramResult> getCostTypeYearDiagramForCurrentYear(Integer year, Long departmentId);

    @Query(value = " SELECT report.department.id AS departmentId, report.department.name AS departmentName, sum(report.totalExpense) AS totalCost FROM AnnualReport annualReport " +
            " JOIN annualReport.monthlyReportSummaries report " +
            " WHERE annualReport.year = :year AND " +
            " annualReport.isDelete = false AND report.isDelete = false " +
            " GROUP BY departmentId, departmentName " +
            " ORDER BY totalCost desc LIMIT 5")
    List<DepartmentDiagramResult> getDepartmentYearDiagram(Integer year);

    @Query(value = " SELECT concat(month (term.finalEndTermDate), '/', year(term.finalEndTermDate)) AS month , report.costType.id AS costTypeId, report.costType.name AS costTypeName, sum(report.totalExpense) AS totalCost FROM ReportStatistical report " +
            " JOIN report.report financialReport " +
            " JOIN financialReport.term term " +
            " WHERE year(term.finalEndTermDate) = :year AND " +
            " (report.department.id = :departmentId OR :departmentId is null) AND " +
            " report.isDelete = false " +
            " GROUP BY costTypeId, costTypeName, concat(month (term.finalEndTermDate), '/', year(term.finalEndTermDate)) ")
    List<CostTypeDiagramResult> getReportCostTypeDiagram(Integer year, Long departmentId);
}

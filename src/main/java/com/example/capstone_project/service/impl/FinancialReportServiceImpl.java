package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.ReportDetailResult;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import com.example.capstone_project.repository.result.ReportExpenseResult;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialReportServiceImpl implements FinancialReportService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final FinancialReportRepository financialReportRepository;
    private final FinancialPlanExpenseRepository expenseRepository;
    private final DepartmentRepository departmentRepository;
    private final CostTypeRepository costTypeRepository;
    private final ExpenseStatusRepository expenseStatusRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final HandleFileHelper handleFileHelper;

    @Override
    public List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            return financialReportRepository.getReportWithPagination(query, termId, statusId, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }

    }

    @Override
    public long countDistinctListReportPaginate(String query, Long termId, Long statusId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            return financialReportRepository.countDistinctListReportPaginate(query, termId, statusId);
        } else {
            throw new UnauthorizedException("Unauthorized to create plan");
        }

    }

    @Override
    public ReportDetailResult getReportDetailByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {

            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }

            return financialReportRepository.getFinancialReportById(reportId);

        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    @Transactional
    public FinancialReport deleteReport(Long reportId) {
        // Check authorization
        if (userAuthorityRepository.get(UserHelper.getUserId()).contains(AuthorityCode.DELETE_REPORT.getValue())) {
            FinancialReport financialReport = financialReportRepository.findById(reportId).orElseThrow(() ->
                    new ResourceNotFoundException("Not found any report have id = " + reportId));
            financialReport.setDelete(true);

            financialReportRepository.save(financialReport);

            return financialReport;
        } else {
            throw new ResourceNotFoundException("Unauthorized to delete report");
        }
    }

//    @Override
//    @Transactional
//    public List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable) throws Exception {
//        // Get userId from token
//        long userId = UserHelper.getUserId();
//        // Get user detail
//        UserDetail userDetail = userDetailRepository.get(userId);
//
//        // Check authority
//        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
//            if (!financialReportRepository.existsById(reportId)) {
//                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
//            }
//
//            FinancialReport report = financialReportRepository.getReferenceById(reportId);
//
//            // Checkout role, accountant can view all plan
//            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
//
//                return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
//
//                // But financial staff can only view plan of their department
//            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
//
//                if (userDetail.getDepartmentId() == report.getDepartment().getId()) {
//
//                    return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
//                } else {
//
//                    throw new UnauthorizedException("User can't view this report because departmentId of plan not equal with departmentId of user");
//                }
//            }
//            throw new UnauthorizedException("Unauthorized to view report");
//        } else {
//            throw new UnauthorizedException("Unauthorized to view report");
//        }
//    }

//    @Override
//    public long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId) {
//        return expenseRepository.countDistinctListExpenseWithPaginate(query, reportId, statusId, costTypeId);
//    }

    @Override
    public byte[] getBodyFileExcelXLSX(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);
        List<Department> departments = departmentRepository.findAll();
        List<CostType> costTypes = costTypeRepository.findAll();
        List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();
        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        }

        return null;
    }

    @Override
    public String generateXLSXFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);
        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_Report.xlsx";
        }
        return null;
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);
        List<Department> departments = departmentRepository.findAll();
        List<CostType> costTypes = costTypeRepository.findAll();
        List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();
        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        }

        return null;
    }

    @Override
    public String generateXLSFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);


        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_Report.xls";
        }

        return null;
    }

    @Override
    public List<ReportExpenseResult> getListExpenseWithPaginate(Long reportId, String query, Integer departmentId, Integer statusId, Integer costTypeId, Pageable pageable) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return expenseRepository.getListExpenseForReport(reportId, query, departmentId, statusId, costTypeId, pageable);

        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer departmentId, Integer statusId, Integer costTypeId) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return expenseRepository.countDistinctListExpenseForReport(query, reportId, departmentId, statusId, costTypeId);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    public BigDecimal calculateActualCostByReportId(Long reportId) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return financialReportRepository.calculateActualCostByReportId(reportId, ExpenseStatusCode.APPROVED);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    public BigDecimal calculateExpectedCostByReportId(Long reportId) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return financialReportRepository.calculateExpectedCostByReportId(reportId);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    private List<ExpenseResult> getListExpenseByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.DOWNLOAD_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return expenseRepository.getListExpenseByReportId(reportId);
        } else {
            throw new UnauthorizedException("Unauthorized to download report");
        }
    }


    @Override
    public void approvalAllExpenses(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            List<FinancialPlanExpense> expenses = expenseRepository.getListExpenseToApprovedByReportId(reportId, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (expenses == null || expenses.isEmpty()) {
                throw new ResourceNotFoundException("Not exist report id = " + reportId + " or list expense is empty");
            }
            expenses.forEach(expense -> {
                expense.setStatus(expenseStatusRepository.getReferenceById(3L));
            });

            expenseRepository.saveAll(expenses);
            // Get plan of this list expense
            FinancialReport report = financialReportRepository.getReferenceById(reportId);
            // Change status to Approved
            report.setStatus(reportStatusRepository.getReferenceById(4L));

            financialReportRepository.save(report);

            expenseRepository.saveAll(expenses);
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    @Transactional
    public void approvalExpenses(Long reportId, List<Long> listExpenses) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one file
            long totalExpense = expenseRepository.countListExpenseInReport(reportId, listExpenses, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenses.size() == totalExpense) {

                listExpenses.forEach(expense -> {
                    if (!expenseRepository.existsById(expense)) {
                        throw new ResourceNotFoundException("Not found expense have id = " + expense);
                    } else {
                        FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
                        updateExpense.setStatus(expenseStatusRepository.getReferenceById(3L));
                        expenses.add(updateExpense);
                    }
                });
                expenseRepository.saveAll(expenses);
                // Get plan of this list expense
                FinancialReport report = financialReportRepository.getReferenceById(reportId);
                // Change status to Reviewed
                report.setStatus(reportStatusRepository.getReferenceById(3L));

                financialReportRepository.save(report);

                expenseRepository.saveAll(expenses);
            } else {
                throw new InvalidInputException("List expense Id invalid ");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    public void denyExpenses(Long planId, List<Long> listExpenseId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one file
            long totalExpense = expenseRepository.countListExpenseInReport(planId, listExpenseId, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenseId.size() == totalExpense) {

                listExpenseId.forEach(expense -> {
                    if (!expenseRepository.existsById(expense)) {
                        throw new ResourceNotFoundException("Not found expense have id = " + expense);
                    } else {
                        FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
                        updateExpense.setStatus(expenseStatusRepository.getReferenceById(4L));
                        expenses.add(updateExpense);
                    }

                });
                expenseRepository.saveAll(expenses);
                // Get plan of this list expense
                FinancialReport report = financialReportRepository.getReferenceById(planId);
                // Change status to Reviewed
                report.setStatus(reportStatusRepository.getReferenceById(3L));

                financialReportRepository.save(report);
                expenseRepository.saveAll(expenses);
            } else {
                throw new InvalidInputException("List expense Id invalid ");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }
}

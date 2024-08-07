package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.*;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.enums.*;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.RemoveDuplicateHelper;
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
import java.util.HashMap;
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

        if (expenses != null && !expenses.isEmpty()) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        } else {
            throw new ResourceNotFoundException("List expenses is empty");
        }
    }

    @Override
    public String generateXLSXFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);
        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_Report.xlsx";
        } else {
            throw new ResourceNotFoundException("Not found any report have id = " + reportId);
        }
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);

        if (expenses != null) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        } else {
            throw new ResourceNotFoundException("List expense is null or empty");
        }
    }

    @Override
    public String generateXLSFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);

        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_Report.xls";
        } else {
            throw new ResourceNotFoundException("Not found report have id = " + reportId);
        }
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

            // Get approval status
            ExpenseStatus approval = expenseStatusRepository.findByCode(ExpenseStatusCode.APPROVED);


            expenses.forEach(expense -> {
                expense.setStatus(approval);
            });

            expenseRepository.saveAll(expenses);
            // Get plan of this list expense
            FinancialReport report = financialReportRepository.getReferenceById(reportId);
            // Change status to Reviewed

            ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

            report.setStatus(reviewedReportStatus);

            financialReportRepository.save(report);

            expenseRepository.saveAll(expenses);
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    public void uploadReportExpenses(Long reportId, List<FinancialPlanExpense> rawExpenses) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

            List<String> listCodes = new ArrayList<>();

            for (FinancialPlanExpense expense : rawExpenses) {
                listCodes.add(expense.getPlanExpenseKey());
            }

            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one file
            long totalExpense = expenseRepository.countListExpenseInReportUpload(reportId, listCodes, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (listCodes.size() == totalExpense) {
                List<ExpenseResult> expenseResults = expenseRepository.getListExpenseInReportUpload(reportId, listCodes);

                HashMap<String, Long> codeAndId = new HashMap<>();
                for (ExpenseResult expenseResult : expenseResults) {
                    codeAndId.put(expenseResult.getExpenseCode(), expenseResult.getExpenseId());
                }

                rawExpenses.forEach(expense -> {

                    FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(codeAndId.get(expense.getPlanExpenseKey()));

                    updateExpense.setStatus(expenseStatusRepository.getReferenceById(expense.getStatus().getId()));

                    expenses.add(updateExpense);

                });
                expenseRepository.saveAll(expenses);
                // Get plan of this list expense
                FinancialReport report = financialReportRepository.getReferenceById(reportId);
                // Change status to Reviewed
                ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

                report.setStatus(reviewedReportStatus);

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
    public List<YearDiagramResult> generateYearDiagram(Integer year) {
        return financialReportRepository.generateYearDiagram(year);
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
            listExpenses = RemoveDuplicateHelper.removeDuplicates(listExpenses);

            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one file
            long totalExpense = expenseRepository.countListExpenseInReport(reportId, listExpenses, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenses.size() == totalExpense) {

                // Get approval status
                ExpenseStatus approval = expenseStatusRepository.findByCode(ExpenseStatusCode.APPROVED);

                listExpenses.forEach(expense -> {
                    FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
                    updateExpense.setStatus(approval);
                    expenses.add(updateExpense);
                });

                expenseRepository.saveAll(expenses);
                // Get plan of this list expense

                FinancialReport report = financialReportRepository.getReferenceById(reportId);
                // Change status to Reviewed

                ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

                report.setStatus(reviewedReportStatus);

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
    public void denyExpenses(Long reportId, List<Long> listExpenseId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {


            listExpenseId = RemoveDuplicateHelper.removeDuplicates(listExpenseId);

            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one file
            long totalExpense = expenseRepository.countListExpenseInReport(reportId, listExpenseId, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenseId.size() == totalExpense) {

                // Get approval status
                ExpenseStatus denyStatus = expenseStatusRepository.findByCode(ExpenseStatusCode.DENIED);

                listExpenseId.forEach(expense -> {

                    FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
                    updateExpense.setStatus(denyStatus);
                    expenses.add(updateExpense);


                });
                expenseRepository.saveAll(expenses);
                // Get plan of this list expense
                FinancialReport report = financialReportRepository.getReferenceById(reportId);
                // Change status to Reviewed
                ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

                report.setStatus(reviewedReportStatus);

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

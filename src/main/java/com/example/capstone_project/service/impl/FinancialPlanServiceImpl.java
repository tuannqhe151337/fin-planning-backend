package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.repository.result.VersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.*;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.reupload.ReUploadExpensesMapperImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    private final FinancialPlanRepository planRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final TermRepository termRepository;
    private final FinancialPlanFileRepository financialPlanFileRepository;
    private final FinancialPlanExpenseRepository expenseRepository;
    private final ExpenseStatusRepository expenseStatusRepository;
    private final DepartmentRepository departmentRepository;
    private final CostTypeRepository costTypeRepository;
    private final HandleFileHelper handleFileHelper;


    @Override
    public long countDistinct(String query, Long termId, Long departmentId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                departmentId = userDetail.getDepartmentId();
            }
            return planRepository.countDistinct(query, termId, departmentId);
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    @Transactional
    public List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Integer pageInt, Integer sizeInt, String sortBy, String sortType) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to view plan");
        } else {
            // Handling pagination
            Pageable pageable = null;
            if (sortBy == null || sortBy.isEmpty()) {
                if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
//                            CustomSort.builder().sortBy(RoleCode.ACCOUNTANT.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));

                } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                    // Default sort of financial staff role
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
//                            CustomSort.builder().sortBy(RoleCode.FINANCIAL_STAFF.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));

                    // Financial staff only see list-plan of their department
                    departmentId = userDetail.getDepartmentId();
                }
            } else {
                if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                    // Financial staff only see list-plan of their department
                    departmentId = userDetail.getDepartmentId();
                }

                // Sort by request
                if (sortBy.equals("id") || sortBy.equals("ID")) {
                    // Sort by id
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(sortBy).sortType(sortType).build()
                    ));

                } else {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(sortBy).sortType(sortType).build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));
                }
            }

            List<FinancialPlan> listResult = planRepository.getPlanWithPagination(query, termId, departmentId, pageable);
            List<PlanVersionResult> listVersions = planRepository.getListPlanVersion(query, termId, departmentId);

            listResult.forEach(plan ->
                    {
                        listVersions.forEach(
                                version -> {
                                    if (Objects.equals(plan.getId(), version.getPlanId())) {
                                        plan.setVersion(version.getVersion());
                                    }
                                }
                        );
                    }
            );

            return listResult;
        }
    }

    @Override
    public List<ReportStatus> getListPlanStatus() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.VIEW_PLAN.getValue())) {

            return reportStatusRepository.findAll(Sort.by(CostType_.ID).ascending());

        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }

    }

    @Override
    @Transactional
    public FinancialPlan createPlan(FinancialPlan plan, Term term) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authorization
        // Check any plan of user department is existing in this term
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to create plan");
        }
        if (termRepository.existsPlanOfDepartmentInTerm(userDetail.getDepartmentId(), plan.getTerm().getId())) {
            throw new DuplicateKeyException("This term already have plan of department id = " + userDetail.getDepartmentId());
        }
        if (!LocalDateTime.now().isBefore(term.getEndDate())) {
            throw new InvalidDateException("Plan due date of this term was expired");
        }
        return planRepository.save(plan);
    }

    public UserDetail getUserDetail() throws Exception {
        return userDetailRepository.get(UserHelper.getUserId());
    }

    @Override
    public Term getTermById(Long termId) {
        termRepository.findById(termId).orElseThrow(() ->
                new ResourceNotFoundException("Not found any term have id = " + termId));

        return termRepository.getReferenceById(termId);
    }

    @Override
    @Transactional
    public FinancialPlan deletePlan(long planId) {
        // Check authorization
        if (userAuthorityRepository.get(UserHelper.getUserId()).contains(AuthorityCode.DELETE_PLAN.getValue())) {

            FinancialPlan financialPlan = planRepository.findById(planId).orElseThrow(() ->
                    new ResourceNotFoundException("Not found any plan have id = " + planId));

            financialPlan.setDelete(true);

            planRepository.save(financialPlan);

            return financialPlan;
        } else {
            throw new UnauthorizedException("Unauthorized to delete plan");
        }
    }

    @Override
    public PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            PlanDetailResult planResult = planRepository.getFinancialPlanById(planId);

            if (planResult == null) {
                throw new ResourceNotFoundException("Not found plan id = " + planId);
            }
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planResult;
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                // Check department
                if (planResult.getDepartmentId() == userDetail.getDepartmentId()) {
                    return planResult;
                } else {
                    throw new UnauthorizedException("User can't view this department because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new UnauthorizedException("Unauthorized to view plan");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public int getPlanVersionById(Long planId) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {

            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found plan id = " + planId);
            }

            return planRepository.getPlanVersionByPlanId(planId);

        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planRepository.getListVersionWithPaginate(planId, pageable);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                FinancialPlan plan = planRepository.getReferenceById(planId);

                // Check department
                if (plan.getDepartment().getId() == userDetail.getDepartmentId()) {
                    return planRepository.getListVersionWithPaginate(planId, pageable);
                } else {
                    throw new UnauthorizedException("User can't view this plan detail because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new UnauthorizedException("Unauthorized to view plan");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public long countDistinctListPlanVersionPaging(Long planId) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            return planRepository.getPlanVersionByPlanId(planId);
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Check exist
            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found any plan have id = " + planId);
            }

            // Checkout role, accountant can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

                return expenseRepository.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);

                // But financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {

                if (userDetail.getDepartmentId() == planRepository.getDepartmentIdByPlanId(planId)) {

                    return expenseRepository.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);
                } else {
                    throw new UnauthorizedException("User can't view this department because departmentId of plan not equal with departmentId of user");
                }
            }
        }
        throw new UnauthorizedException("Unauthorized to view plan");
    }

    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            return expenseRepository.countDistinctListExpenseWithPaginate(query, planId, statusId, costTypeId);
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

//    @Override
//    @Transactional
//    public void approvalExpenses(Long planId, List<Long> listExpenses) throws Exception {
//        // Get userId from token
//        long userId = UserHelper.getUserId();
//
//        // Get user detail
//        UserDetail userDetail = userDetailRepository.get(userId);
//
//        // Check authority
//        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
//            List<FinancialPlanExpense> expenses = new ArrayList<>();
//            // Check list expense in one file
//            long totalExpense = expenseRepository.countTotalExpenseInPlanLastVersion(planId, listExpenses, TermCode.IN_PROGRESS, LocalDateTime.now());
//            if (listExpenses.size() == totalExpense) {
//
//                listExpenses.forEach(expense -> {
//                    if (!expenseRepository.existsById(expense)) {
//                        throw new ResourceNotFoundException("Not found expense have id = " + expense);
//                    } else {
//                        FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
//                        updateExpense.setStatus(expenseStatusRepository.getReferenceById(3L));
//                        expenses.add(updateExpense);
//                    }
//                });
//                expenseRepository.saveAll(expenses);
//                // Get plan of this list expense
//                FinancialPlan plan = planRepository.getReferenceById(planId);
//                // Change status to Reviewed
////                plan.setStatus(planStatusRepository.getReferenceById(3L));
//
//                planRepository.save(plan);
//
//                expenseRepository.saveAll(expenses);
//            } else {
//                throw new InvalidInputException("List expense Id invalid ");
//            }
//        } else {
//            throw new UnauthorizedException("Unauthorized to approval plan");
//        }
//    }
//
//    @Override
//    public void denyExpenses(Long planId, List<Long> listExpenseId) throws Exception {
//        // Get userId from token
//        long userId = UserHelper.getUserId();
//
//        // Get user detail
//        UserDetail userDetail = userDetailRepository.get(userId);
//
//        // Check authority
//        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
//            List<FinancialPlanExpense> expenses = new ArrayList<>();
//            // Check list expense in one file
//            long totalExpense = expenseRepository.countTotalExpenseInPlanLastVersion(planId, listExpenseId, TermCode.IN_PROGRESS, LocalDateTime.now());
//            if (listExpenseId.size() == totalExpense) {
//
//                listExpenseId.forEach(expense -> {
//                    if (!expenseRepository.existsById(expense)) {
//                        throw new ResourceNotFoundException("Not found expense have id = " + expense);
//                    } else {
//                        FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
//                        updateExpense.setStatus(expenseStatusRepository.getReferenceById(4L));
//                        expenses.add(updateExpense);
//                    }
//
//                });
//                expenseRepository.saveAll(expenses);
//                // Get plan of this list expense
//                FinancialPlan plan = planRepository.getReferenceById(planId);
//                // Change status to Reviewed
////                plan.setStatus(planStatusRepository.getReferenceById(3L));
//
//                planRepository.save(plan);
//                expenseRepository.saveAll(expenses);
//            } else {
//                throw new InvalidInputException("List expense Id invalid ");
//            }
//        } else {
//            throw new UnauthorizedException("Unauthorized to approval plan");
//        }
//    }

    @Override
    public byte[] getBodyFileExcelXLS(Long fileId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByFileId(fileId);

        if (expenses != null && !expenses.isEmpty()) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        } else {
            throw new ResourceNotFoundException("Not exist file = " + fileId + " or list expenses is empty");
        }
    }

    @Override
    public byte[] getBodyFileExcelXLSX(Long fileId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByFileId(fileId);

        if (expenses != null && !expenses.isEmpty()) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        } else {
            throw new ResourceNotFoundException("Not exist file = " + fileId + " or list expenses is empty");
        }
    }

    private List<ExpenseResult> getListExpenseByFileId(Long fileId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.DOWNLOAD_PLAN.getValue())) {
            long departmentId = departmentRepository.getDepartmentIdByFileId(fileId);

            // Check department
            if (departmentId == userDetail.getDepartmentId()) {
                return planRepository.getListExpenseByFileId(fileId);
            } else {
                throw new UnauthorizedException("User can't download this plan because departmentId of plan not equal with departmentId of user");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to download plan");
        }
    }

    @Override
    public String generateXLSXFileName(Long fileId) {
        int planId = planRepository.getPlanIdByFileId(fileId);
        List<FileNameResult> fileNameResultList = financialPlanFileRepository.generateFileName(planId);

        if (fileNameResultList == null) {
            throw new ResourceNotFoundException("Not exist file id = " + fileId);
        }

        for (FileNameResult fileName : fileNameResultList) {
            if (Objects.equals(fileName.getFileId(), fileId)) {
                return fileName.getTermName() + "_v" + fileName.getVersion() + ".xlsx";
            }
        }

        return null;
    }

    @Override
    public String generateXLSFileName(Long fileId) {
        int planId = planRepository.getPlanIdByFileId(fileId);
        List<FileNameResult> fileNameResultList = financialPlanFileRepository.generateFileName(planId);

        if (fileNameResultList == null) {
            throw new ResourceNotFoundException("Not exist file id = " + fileId);
        }

        for (FileNameResult fileName : fileNameResultList) {
            if (Objects.equals(fileName.getFileId(), fileId)) {
                return fileName.getTermName() + "_v" + fileName.getVersion() + ".xls";
            }
        }
        return null;
    }

    @Override
    public FinancialPlan convertListExpenseAndMapToPlan(Long planId, List<ReUploadExpenseBody> expenseBodies) throws Exception {

        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authorization
        // Check any plan of user department is existing in this term
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.RE_UPLOAD_PLAN.getValue())) {

            // Check exist
            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found any plan have id = " + planId);
            }

            Term term = termRepository.getTermByPlanId(planId);

            if ((LocalDateTime.now().isAfter(term.getStartDate()) && LocalDateTime.now().isBefore(term.getEndDate()))
                    || (LocalDateTime.now().isAfter(term.getReuploadStartDate()) && LocalDateTime.now().isBefore(term.getReuploadEndDate()))) {
                long departmentId = planRepository.getDepartmentIdByPlanId(planId);
                // Check department
                if (departmentId == userDetail.getDepartmentId()) {
                    List<ExpenseResult> listExpenseCreate = expenseRepository.getListExpenseByPlanId(planId);

                    // Check null and empty
                    if (listExpenseCreate == null || listExpenseCreate.isEmpty()) {
                        throw new ResourceNotFoundException("List expense is empty");
                    }

                    // Handle list expense
                    HashMap<String, ExpenseStatusCode> hashMapExpense = new HashMap<>();
                    List<FinancialPlanExpense> listExpense = new ArrayList<>();

                    // Get last code of expense by plan Id
                    String lastExpenseCode = expenseRepository.getLastExpenseCode(planId);
                    String[] parts = lastExpenseCode.split("_");

                    // Create prefix expense code
                    StringBuilder prefixExpenseKey = new StringBuilder();
                    for (int i = 0; i < parts.length - 2; i++) {
                        prefixExpenseKey.append(parts[i] + "_");
                    }

                    int lastIndexCode = Integer.parseInt(parts[parts.length - 1]);

                    // Get current version by plan Id
                    PlanVersionResult version = planRepository.getCurrentVersionByPlanId(planId);

                    // Split list expense depend on status code
                    for (ExpenseResult expenseResult : listExpenseCreate) {
                        if (expenseResult.getStatusCode().equals(ExpenseStatusCode.APPROVED)) {
                            hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.APPROVED);

                            // Add old expenses had approved in old version
                            listExpense.add(expenseRepository.getReferenceById(expenseResult.getExpenseId()));
                        } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.NEW)) {
                            hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.NEW);
                        } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.DENIED)) {
                            hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.DENIED);
                        }
                    }

                    // Handle new expenses need to re-upload
                    for (ReUploadExpenseBody expenseBody : expenseBodies) {

                        // If exist expense code and status not approve, update expense
                        if (hashMapExpense.containsKey(expenseBody.getExpenseCode()) &&
                                !hashMapExpense.get(expenseBody.getExpenseCode()).getValue().equals(ExpenseStatusCode.APPROVED.getValue())
                        ) {
                            listExpense.add(new ReUploadExpensesMapperImpl().mapUpdateExpenseToPlanExpense(expenseBody));

                            // If not exist expense code, create new expense
                        } else if (!hashMapExpense.containsKey(expenseBody.getExpenseCode())) {
                            listExpense.add(new ReUploadExpensesMapperImpl().newExpenseToPlanExpense(expenseBody, prefixExpenseKey, version.getVersion(), ++lastIndexCode));
                        }
                    }

                    // Map to plan
                    FinancialPlan plan = planRepository.getReferenceById(planId);
                    return new ReUploadExpensesMapperImpl().mapToPlanMapping(plan, (long) UserHelper.getUserId(), version, listExpense);
                } else {
                    throw new UnauthorizedException("User can't upload this plan because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new InvalidDateException("Can not re-upload plan in this time period");
            }

        } else {
            throw new UnauthorizedException("Unauthorized to re-upload plan");
        }
    }

    @Override
    @Transactional
    public void reUploadPlan(FinancialPlan plan) {
        planRepository.save(plan);
    }


    @Override
    public byte[] getLastVersionBodyFileExcelXLS(Long planId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByPlanId(planId);

        if (expenses != null) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        } else {
            throw new ResourceNotFoundException("Not exist plan id = " + planId + " or list expenses is empty");
        }
    }

    private List<ExpenseResult> getListExpenseByPlanId(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.DOWNLOAD_PLAN.getValue())) {

            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found any plan have id = " + planId);
            }

            long departmentId = departmentRepository.getDepartmentIdByPlanId(planId);

            // Check department
            if (departmentId == userDetail.getDepartmentId()) {
                return planRepository.getListExpenseByPlanId(planId);
            } else {
                throw new UnauthorizedException("User can't download this plan because departmentId of plan not equal with departmentId of user");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to download plan");
        }
    }

    @Override
    public String generateXLSFileNameByPlanId(Long planId) {
        FileNameResult fileNameResult = financialPlanFileRepository.getLastVersionFileName(planId);

        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_v" + fileNameResult.getVersion() + ".xls";
        } else {
            throw new ResourceNotFoundException("Not found any file of plan have id = " + planId);
        }
    }

    @Override
    public byte[] getLastVersionBodyFileExcelXLSX(Long planId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByPlanId(planId);

        if (expenses != null) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses);
        } else {
            throw new ResourceNotFoundException("Not exist plan id = " + planId + " or list expenses is empty");
        }
    }

    @Override
    public String generateXLSXFileNameByPlanId(Long planId) {
        FileNameResult fileNameResult = financialPlanFileRepository.getLastVersionFileName(planId);

        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_v" + fileNameResult.getVersion() + ".xlsx";
        } else {
            throw new ResourceNotFoundException("Not found any file of plan have id = " + planId);
        }
    }

//    @Override
//    public void approvalAllExpenses(Long planId) throws Exception {
//        // Get userId from token
//        long userId = UserHelper.getUserId();
//
//        // Get user detail
//        UserDetail userDetail = userDetailRepository.get(userId);
//
//        // Check authority
//        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
//            List<FinancialPlanExpense> expenses = expenseRepository.getListExpenseByPlanId(planId, TermCode.IN_PROGRESS, LocalDateTime.now());
//            if (expenses == null || expenses.isEmpty()) {
//                throw new ResourceNotFoundException("Not exist plan id = " + planId + " or list expense is empty");
//            }
//            expenses.forEach(expense -> {
//                expense.setStatus(expenseStatusRepository.getReferenceById(3L));
//            });
//
//            expenseRepository.saveAll(expenses);
//            // Get plan of this list expense
//            FinancialPlan plan = planRepository.getReferenceById(planId);
//            // Change status to Approved
////            plan.setStatus(planStatusRepository.getReferenceById(4L));
//
//            planRepository.save(plan);
//
//            expenseRepository.saveAll(expenses);
//        } else {
//            throw new UnauthorizedException("Unauthorized to approval plan");
//        }
//    }


    @Override
    public List<ExpenseStatus> getListExpenseStatus() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.VIEW_PLAN.getValue())) {

            return expenseStatusRepository.findAll(Sort.by(CostType_.ID).ascending());

        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public void submitPlanForReview(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);
        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.SUBMIT_PLAN_FOR_REVIEW.getValue())) {
            // Check exist
            FinancialPlan plan = planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Not found any plan have id = " + planId));
            // Check department
            if (plan.getDepartment().getId() == userDetail.getDepartmentId()) {
                List<FinancialPlanExpense> listExpenseNeedToSubmit = new ArrayList<>();
                expenseRepository.getListExpenseNewInLastVersion(planId).forEach(expense -> {
                    // Change expense status from new to waiting for approval - 2L
                    expense.setStatus(expenseStatusRepository.getReferenceById(2L));
                    listExpenseNeedToSubmit.add(expense);
                });
                expenseRepository.saveAll(listExpenseNeedToSubmit);
                // Change plan status
//                plan.setStatus(planStatusRepository.getReferenceById(2L));
                planRepository.save(plan);
            } else {
                throw new UnauthorizedException("User can't submit this plan because departmentId of plan not equal with departmentId of user");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public byte[] getTemplateData() throws IOException {
        List<Department> departments = departmentRepository.findAll();
        List<CostType> costTypes = costTypeRepository.findAll();
        List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();

        String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
        FileInputStream file = new FileInputStream(fileLocation);
        XSSFWorkbook wb = new XSSFWorkbook(file);


        Sheet sheet = wb.getSheet("List");

        Row row = null;
        // Write department
        int rowPosition = 2;
        int colPosition;

        for (Department department : departments) {
            colPosition = 0;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);

            row.createCell(colPosition++).setCellValue(department.getId());
            row.createCell(colPosition).setCellValue(department.getName());
        }

        // Write cost type
        rowPosition = 2;

        for (CostType costType : costTypes) {
            colPosition = 3;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(costType.getId());
            row.createCell(colPosition).setCellValue(costType.getName());
        }

        // Write expense status
        rowPosition = 2;

        for (ExpenseStatus status : expenseStatuses) {
            colPosition = 6;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(status.getId());
            row.createCell(colPosition).setCellValue(status.getCode().toString());
        }

        // Add validation
        sheet = wb.getSheet("Expense");
        // Add validation for department

        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createFormulaListConstraint("List!$B$3:$B$" + (departments.size() + 2));

        CellRangeAddressList addressList = new CellRangeAddressList(2, 100, 3, 3);
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for department

        constraint = validationHelper.createFormulaListConstraint("List!$E$3:$E$" + (costTypes.size() + 2));

        addressList = new CellRangeAddressList(2, 100, 5, 5);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for expense status

        constraint = validationHelper.createFormulaListConstraint("List!$H$3:$H$" + (expenseStatuses.size() + 2));

        addressList = new CellRangeAddressList(2, 100, 13, 13);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();

        return out.toByteArray();
    }
}

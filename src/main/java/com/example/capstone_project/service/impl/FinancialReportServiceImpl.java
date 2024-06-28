package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.FinancialReportExpense;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.FinancialReportExpenseRepository;
import com.example.capstone_project.repository.FinancialReportRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialReportServiceImpl implements FinancialReportService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final FinancialReportRepository financialReportRepository;
    private final FinancialReportExpenseRepository expenseRepository;

    @Override
    public List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                // Financial staff only see list-plan of their department
                departmentId = userDetail.getDepartmentId();
            }

            return financialReportRepository.getReportWithPagination(query, termId, departmentId, statusId, pageable);
        }

        return null;

    }

    @Override
    public long countDistinctListReportPaginate(String query, Long termId, Long departmentId, Long statusId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())
                && userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
            departmentId = userDetail.getDepartmentId();
        }

        return financialReportRepository.countDistinctListReportPaginate(query, termId, departmentId, statusId);
    }

    @Override
    @Transactional
    public List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        FinancialReport report = financialReportRepository.getReferenceById(reportId);
        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Checkout role, accountant can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

                return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);

                // But financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())
                    && userDetail.getDepartmentId() == report.getDepartment().getId()) {

                return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
            }
        }
        return null;
    }

    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId) {
        return expenseRepository.countDistinctListExpenseWithPaginate(query, reportId, statusId, costTypeId);
    }


}

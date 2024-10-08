package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.entity.*;

import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.ReportStatusCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.enums.TermStatusCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.*;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TermServiceImpl implements TermService {
    private final TermRepository termRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserRepository userRepository;
    private final TermStatusRepository termStatusRepository;
    private final TermIntervalRepository termIntervalRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final FinancialReportRepository financialReportRepository;

    @Override
    public long countDistinct(String query) throws Exception {
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        return termRepository.countDistinctListTermWhenCreatePlan(query, TermStatusCode.CLOSED, LocalDateTime.now(), userDetail.getDepartmentId());
    }

    @Override
    public long countDistinctListTermWhenCreatePlan(String query) throws Exception {
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        return termRepository.countDistinctListTermWhenCreatePlan(query, TermStatusCode.CLOSED, LocalDateTime.now(), userDetail.getDepartmentId());
    }

    @Override
    public List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) throws Exception {
        // Get userId
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            return termRepository.getListTermWhenCreatePlan(query, pageable, userDetail.getDepartmentId());
        } else {
            throw new UnauthorizedException("User authority to create new plan");
        }
    }

    @Override

    public Term updateTerm(Term term) throws Exception {

        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to update term");
        }

        if (!term.isAllowReupload()) {
            term.setReuploadStartDate(null);
            term.setReuploadEndDate(null);
        }

        checkValidDateOfTerm(term);

        //calculate final end term date
        LocalDateTime startDate = term.getStartDate();
        LocalDateTime finalEndTermDate = term.getDuration().calculateEndDate(startDate);
        term.setFinalEndTermDate(finalEndTermDate);

        Term currentterm = termRepository.findById(term.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Term not exist with id: " + term.getId()));
        //status
        term.setStatus(currentterm.getStatus());

        //create-by
        User userby = userRepository.findUserById(userId).get();
        term.setUser(userby);

        return termRepository.save(term);
    }

    @Override
    public void deleteTerm(Long id) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.DELETE_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }
        Term currentTerm = termRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Term not exist with id: " + id));

        if (!currentTerm.getStatus().getCode().equals(TermStatusCode.NEW)) {
            throw new InvalidDateException("Only can delete term when status is new");
        }
        currentTerm.setDelete(true);
        termRepository.save(currentTerm);

    }

    @Override
    public Term findTermById(Long id) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to access this resource");
        }
        Term term = termRepository.findTermById(id);
        if (term == null) {
            throw new ResourceNotFoundException("Term not found");
        } else {
            return term;
        }

    }


    @Override
    public void startTermManually(Long termId) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.START_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to start term");
        }
        Term term = termRepository.findTermById(termId);
        if (term == null) {
            throw new ResourceNotFoundException("Term not found");
        }

        TermStatus termStatus = termStatusRepository.findByCode(TermStatusCode.IN_PROGRESS);

        term.setStatus(termStatus);
        term.setStartDate(LocalDateTime.now());
        termRepository.save(term);

        ReportStatus newStatus = reportStatusRepository.findByCode(ReportStatusCode.NEW);

        // Create new report
        FinancialReport report = FinancialReport.builder()
                .name(term.getName() + "_" + "Report")
                .month(term.getFinalEndTermDate().toLocalDate())
                .term(term)
                .status(newStatus)
                .build();

        // Generate report
        financialReportRepository.save(report);

    }


    @Override
    public void createTerm(Term term) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.CREATE_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to create term");
        }
        if (!term.isAllowReupload()) {
            term.setReuploadStartDate(null);
            term.setReuploadEndDate(null);
        }

        checkValidDateOfTerm(term);

        LocalDateTime finalEndTermDate;
        finalEndTermDate = term.getDuration().calculateEndDate(term.getStartDate());
        term.setFinalEndTermDate(finalEndTermDate);
        //status-id , create-by
        TermStatus status = termStatusRepository.getReferenceById(1L);
        term.setStatus(status);

        User user = userRepository.getReferenceById(userId);
        term.setUser(user);


        termRepository.save(term);

    }

    @Override
    public List<Term> getListTermPaging(Long statusId, String query, Integer pageInt, Integer sizeInt, String sortBy, String sortType) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())
                || userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_TERM.getValue())) {
            Pageable pageable = null;
            if (sortBy == null || sortBy.isEmpty()) {
                pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                        CustomSort.builder().sortBy(Term_.STATUS).sortType("asc").build(),
                        CustomSort.builder().sortBy(Term_.START_DATE).sortType("desc").build(),
                        CustomSort.builder().sortBy(Term_.ID).sortType("desc").build()
                ));
            } else {
                // Sort by request
                if (sortBy.equals("id") || sortBy.equals("ID")) {
                    // Sort by id
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(sortBy).sortType(sortType).build()
                    ));

                } else {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(sortBy).sortType(sortType).build(),
                            CustomSort.builder().sortBy(Term_.ID).sortType("desc").build()
                    ));
                }
            }
            return termRepository.getListTermPaging(statusId, query, pageable);

        }

        return null;
    }

    @Override
    public long countDistinctListTermPaging(Long statusId, String query) {
        return termRepository.countDistinctListTermPaging(statusId, query);
    }

    // Check valid date of term
    public void checkValidDateOfTerm(Term term) throws Exception {
        LocalDateTime finalEndTermDate;
        finalEndTermDate = term.getDuration().calculateEndDate(term.getStartDate());
        TermInterval termInterval = termIntervalRepository.getReferenceById(1);
        //check start term date must in 25-30
        if (!(term.getStartDate().getDayOfMonth() >= termInterval.getStartTermDate()) &&
                (term.getStartDate().getDayOfMonth() <= (termInterval.getStartTermDate() + termInterval.getEndTermInterval()))) {
            throw new InvalidStartTermDateException("Start date must be in day " + termInterval.getStartTermDate());
        }

        if (finalEndTermDate.isBefore(LocalDateTime.now())) {
            throw new InvalidEndDateException("Final end date must be in the future");
        }

        // throw exception if end date is before start date
        //after 5 days from start day - false
        LocalDateTime boundaryEndDate = term.getStartDate().plusDays(termInterval.getEndTermInterval());
        if (!term.getEndDate().isAfter(term.getStartDate()) || term.getEndDate().isAfter(finalEndTermDate)
                || term.getEndDate().isAfter(boundaryEndDate)) {
            throw new InvalidEndDateException("End date must be within " +
                    term.getStartDate().toLocalDate().plusDays(1) + " and " + boundaryEndDate.toLocalDate());
        }
        //throw exception if start reup date is before end date
        //before 20 days - false
        if (term.isAllowReupload()) {
            LocalDate date = LocalDate.of(term.getStartDate().getYear(), term.getStartDate().getMonth(), termInterval.getStartTermDate());
            LocalTime time = LocalTime.of(term.getStartDate().getHour(), term.getStartDate().getMinute());
            LocalDateTime startdate = LocalDateTime.of(date, time);
            LocalDateTime boundaryStartReuploadDate = startdate.plusDays(termInterval.getStartReuploadInterval());
            if (!term.getReuploadStartDate().isAfter(term.getEndDate()) || term.getReuploadStartDate().isAfter(finalEndTermDate)
                    || term.getReuploadStartDate().isBefore(boundaryStartReuploadDate)) {
                throw new InvalidStartReupDateException("Re-upload start date must be within "
                        + boundaryStartReuploadDate.toLocalDate() + " and " +
                        boundaryStartReuploadDate.plusDays(termInterval.getEndReuploadInterval()).toLocalDate());
            }
            //throw ex if end reup date is before start reup date
            //after 3 days vs start re up date - false
            LocalDateTime boundaryReuploadEndDate = term.getReuploadStartDate().plusDays(termInterval.getEndReuploadInterval());
            if (!term.getReuploadEndDate().isAfter(term.getReuploadStartDate()) ||
                    term.getReuploadEndDate().isAfter(finalEndTermDate) ||
                    term.getReuploadEndDate().isAfter(boundaryReuploadEndDate)) {
                throw new InvalidEndReupDateException("Re-upload end date must be within " +
                        boundaryStartReuploadDate.toLocalDate().plusDays(1) + " and " + boundaryReuploadEndDate.toLocalDate());
            }
        }
    }


}

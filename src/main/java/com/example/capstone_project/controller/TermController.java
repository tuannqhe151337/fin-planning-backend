package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.start.StartTermBody;
import com.example.capstone_project.controller.body.term.create.CreateTermBody;
import com.example.capstone_project.controller.body.term.delete.DeleteTermBody;
import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.term.getPlans.PlanStatusResponse;
import com.example.capstone_project.controller.responses.term.getPlans.TermPlanDetailResponse;
import com.example.capstone_project.controller.responses.term.getReports.TermReportResponse;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermDetailResponse;

import com.example.capstone_project.controller.responses.term.selectWhenCreatePlan.TermWhenCreatePlanResponse;
import com.example.capstone_project.controller.responses.term.termInterval.TermIntervalResponse;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.entity.Term;

import com.example.capstone_project.entity.TermInterval;
import com.example.capstone_project.service.TermIntervalService;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.*;

import com.example.capstone_project.utils.exception.ResourceNotFoundException;

import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.term.create.CreateTermBodyToTermEntityMapperImpl;
import com.example.capstone_project.utils.mapper.term.detail.TermToTermDetailResponseMapperImpl;
import com.example.capstone_project.utils.mapper.term.paginate.TermPaginateResponseMapperImpl;
import com.example.capstone_project.utils.mapper.term.selectWhenCreatePlan.TermWhenCreatePlanMapperImpl;


import com.example.capstone_project.utils.mapper.term.termInterval.TermIntervalMapperImpl;
import com.example.capstone_project.utils.mapper.term.update.UpdateTermBodyToTermEntityMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.term.paginate.TermPaginateResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term")
@Validated
public class TermController {
    private final TermService termService;
    private final TermIntervalService termIntervalService;


    @GetMapping("/report")
    public ResponseEntity<ListPaginationResponse<TermReportResponse>> getReportListByTerm
            (@RequestParam(name = "termId") Long termId,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String sortBy,
             @RequestParam(required = false) String sortType) {
        TermReportResponse rp1 = TermReportResponse.builder()
                .id(1L).createdAt(LocalDateTime.now()).name("REPORT 1 TERM SPRING").build();
        TermReportResponse rp2 = TermReportResponse.builder()
                .id(2L).createdAt(LocalDateTime.now()).name("REPORT 2 TERM WINTER").build();
        TermReportResponse rp3 = TermReportResponse.builder()
                .id(3L).createdAt(LocalDateTime.now()).name("REPORT 3 TERM FALL").build();
        List<TermReportResponse> rps = new ArrayList<>();
        rps.add(rp1);
        rps.add(rp2);
        rps.add(rp3);
        // Sort the list by updatedAt, from newest to oldest
        Collections.sort(rps, new Comparator<TermReportResponse>() {
            @Override
            public int compare(TermReportResponse o1, TermReportResponse o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });

        PageRequest pageRequest = (PageRequest) PaginationHelper.handlingPagination(page, size, sortBy, sortType);

        //Tao Page tu list
        Page<TermReportResponse> listTermReport = PaginationHelper.createPage(rps, pageRequest);

        //Build response
        Pagination pagination = Pagination
                .builder()
                .page(pageRequest.getPageNumber())
                .limitRecordsPerPage(pageRequest.getPageSize())
                .totalRecords((long) listTermReport.getNumberOfElements())
                .numPages(PaginationHelper.
                        calculateNumPages((long) listTermReport.getNumberOfElements(),
                                pageRequest.getPageSize())).build();

        ListPaginationResponse<TermReportResponse> response = new ListPaginationResponse<>();
        response.setData(rps);
        response.setPagination(pagination);


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/plan")
    public ResponseEntity<ListPaginationResponse<TermPlanDetailResponse>> getPlanListByTerm
            (@RequestParam(name = "termId") Long termId,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String sortBy,
             @RequestParam(required = false) String sortType) {

        TermPlanDetailResponse termplan =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 1")
                        .planStatus(PlanStatusResponse.builder().id(1L).code("REVIEWED").name("REVIEWED").build())
                        .build();
        termplan.setCreatedAt(LocalDateTime.now());
        termplan.setUpdatedAt(LocalDateTime.now());

        TermPlanDetailResponse termplan2 =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 2")
                        .planStatus(PlanStatusResponse.builder().id(1L).code("REVIEWED").name("REVIEWED").build())
                        .build();
        termplan2.setCreatedAt(LocalDateTime.now());
        termplan2.setUpdatedAt(LocalDateTime.of(2025, 11, 6, 0, 0, 0));

        TermPlanDetailResponse termplan3 =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 3")
                        .planStatus(PlanStatusResponse.builder().id(1L).code("REVIEWED").name("REVIEWED").build())
                        .build();
        termplan3.setCreatedAt(LocalDateTime.now());
        termplan3.setUpdatedAt(LocalDateTime.of(2026, 11, 6, 0, 0, 0));

        List<TermPlanDetailResponse> list = new ArrayList<>();
        list.add(termplan);
        list.add(termplan2);
        list.add(termplan3);

        // Sort the list by updatedAt, from newest to oldest
        Collections.sort(list, new Comparator<TermPlanDetailResponse>() {
            @Override
            public int compare(TermPlanDetailResponse o1, TermPlanDetailResponse o2) {
                return o2.getUpdatedAt().compareTo(o1.getUpdatedAt());
            }
        });

        PageRequest pageRequest = (PageRequest) PaginationHelper.handlingPagination(page, size, sortBy, sortType);

        //Tao Page tu list
        Page<TermPlanDetailResponse> listTermPlan = PaginationHelper.createPage(list, pageRequest);


        //Build response
        Pagination pagination = Pagination
                .builder()
                .page(pageRequest.getPageNumber())
                .limitRecordsPerPage(pageRequest.getPageSize())
                .totalRecords((long) listTermPlan.getNumberOfElements())
                .numPages(PaginationHelper.
                        calculateNumPages((long) listTermPlan.getNumberOfElements(),
                                pageRequest.getPageSize())).build();

        ListPaginationResponse<TermPlanDetailResponse> response = new ListPaginationResponse<>();
        response.setData(list);
        response.setPagination(pagination);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getTermDetailById(@PathVariable("id") Long id) {
        try {
            TermDetailResponse termDetailResponse = new TermToTermDetailResponseMapperImpl()
                    .mapTermToTermDetailResponse(termService.findTermById(id));
            return ResponseEntity.status(HttpStatus.OK).body(termDetailResponse);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("error").message("Term not found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createTerm(@Valid @RequestBody CreateTermBody createTermBody) {
        //map create term body to term entity
        Term term = new CreateTermBodyToTermEntityMapperImpl().mapCreateTermBodyToTermEntity(createTermBody);
        try {
            termService.createTerm(term);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("Authorization").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
        } catch (InvalidEndDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("endDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidEndReupDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("reuploadEndDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidStartReupDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("reuploadStartDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidStartTermDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("startDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping
    public ResponseEntity<Object> updateTerm(@Valid @RequestBody UpdateTermBody updateTermBody, BindingResult result) {

        //map create term body to term entity
        Term term = new UpdateTermBodyToTermEntityMapperImpl().mapUpdateTermBodyToTermEntity(updateTermBody);
        try {
            term = termService.updateTerm(term);
            //map term to term detail response
            TermDetailResponse termDetailResponse = new TermToTermDetailResponseMapperImpl().mapTermToTermDetailResponse(term);
            return ResponseEntity.status(HttpStatus.OK).body(termDetailResponse);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (InvalidEndDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("endDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidEndReupDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("reuploadEndDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidStartReupDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("reuploadStartDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidStartTermDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("startDate").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @DeleteMapping
    public ResponseEntity<Object> deleteTerm(@Valid @RequestBody DeleteTermBody deleteTermBody, BindingResult result) {
        try {
            termService.deleteTerm(deleteTermBody.getId());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("error").message("Term not found")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (InvalidDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("error").message("Only can delete term when status is new")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/plan-paging-term")
    public ResponseEntity<ListPaginationResponse<TermPaginateResponse>> getListTermPaging(
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        // Handling page and pageSize
        Integer pageInt = PaginationHelper.convertPageToInteger(page);
        Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling query
        if (query == null) {
            query = "";
        }

        // Get data
        List<Term> terms = termService.getListTermPaging(statusId, query, pageInt, sizeInt, sortBy, sortType);

        // Response
        ListPaginationResponse<TermPaginateResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (terms != null) {

            // Count total record
            count = termService.countDistinctListTermPaging(statusId, query);

            // Mapping to TermPaginateResponse
            terms.forEach(term -> response.getData().add(new TermPaginateResponseMapperImpl().mapToTermPaginateResponseMapper(term)));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .totalRecords(count)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/plan-create-select-term")
    public ResponseEntity<ListPaginationResponse<TermWhenCreatePlanResponse>> getListTermWhenCreatePlan(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
        // Handling page and pageSize
        Integer pageInt = PaginationHelper.convertPageToInteger(page);
        Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling query
        if (query == null) {
            query = "";
        }

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<Term> terms = termService.getListTermWhenCreatePlan(query, pageable);

        // Response
        ListPaginationResponse<TermWhenCreatePlanResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (terms != null) {

            // Count total record
            count = termService.countDistinctListTermWhenCreatePlan(query);

            // Mapping to TermPaginateResponse
            terms.forEach(term -> response.getData().add(new TermWhenCreatePlanMapperImpl().mapToTermWhenCreatePlanResponseMapper(term)));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .totalRecords(count)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<Object> startTermManually(@Valid @RequestBody StartTermBody startTermBody, BindingResult result) {
        try {
            termService.startTermManually(startTermBody.getTermId());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("error").message("Term not found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/termInterval")
    public ResponseEntity<Object> getTermInterval() {
        TermInterval termInterval = termIntervalService.getTermInterval();
        TermIntervalResponse termIntervalResponse = new TermIntervalMapperImpl().mapToTermIntervalResponse(termInterval);
        return ResponseEntity.status(HttpStatus.OK).body(termIntervalResponse);
    }
}

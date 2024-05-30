package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.planDetail.PlanDetailResponse;
import com.example.capstone_project.controller.responses.planDetail.StatusPlanDetailResponse;
import com.example.capstone_project.controller.responses.plan.list.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.list.StatusResponse;
import com.example.capstone_project.controller.responses.plan.list.TermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {
    @GetMapping("list")
    public ResponseEntity<ListResponse<PlanResponse>> getListPlan(
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ){
        ListResponse<PlanResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                PlanResponse.builder()
                        .id(1L)
                        .name("BU name_term_plan")
                        .status(StatusResponse.builder()
                                .statusId(1L)
                                .name("New").build())
                        .term(TermResponse.builder()
                                .termId(1L)
                                .name("Term name 1").build())
                        .department(DepartmentResponse.builder()
                                .departmentId(1L)
                                .name("BU 1").build())
                        .version("V1").build(),
                PlanResponse.builder()
                        .id(2L)
                        .name("BU name_term_plan")
                        .status(StatusResponse.builder()
                                .statusId(2L)
                                .name("Approved").build())
                        .term(TermResponse.builder()
                                .termId(1L)
                                .name("Term name 1").build())
                        .department(DepartmentResponse.builder()
                                .departmentId(2L)
                                .name("BU 2").build())
                        .version("V2").build()
                ));

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("detail")
    public ResponseEntity<PlanDetailResponse> getDetailPlan(
            @RequestParam Integer planId
    ){
        return ResponseEntity.ok(PlanDetailResponse.builder()
                        .planId(1L)
                        .termName("Financial plan december Q3 2021")
                        .biggestExpenditure(BigDecimal.valueOf(180000000))
                        .totalPlan(BigDecimal.valueOf(213425384))
                        .planDueDate(LocalDate.now())
                        .departmentName("BU 01")
                        .status(StatusPlanDetailResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval")
                                .build())
                        .version("2")
                        .createAt(LocalDate.of(2024,3,2))
                        .createBy("AnhLN2")
                .build());
    }
}

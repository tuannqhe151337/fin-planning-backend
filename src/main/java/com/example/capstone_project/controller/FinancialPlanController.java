package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.expense.ApprovalExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.body.ListBody;
import com.example.capstone_project.controller.body.plan.detail.PlanDetailBody;
import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
import com.example.capstone_project.controller.body.plan.delete.DeletePlanBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.responses.*;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.create.CreatePlanMapperImpl;
import com.example.capstone_project.utils.mapper.expense.CostTypeMapperImpl;
import com.example.capstone_project.utils.mapper.plan.detail.PlanDetailMapperImpl;
import com.example.capstone_project.utils.mapper.plan.list.ListPlanResponseMapperImpl;
import com.example.capstone_project.utils.mapper.plan.status.PlanStatusMapper;
import com.example.capstone_project.utils.mapper.plan.status.PlanStatusMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {

    private final FinancialPlanService planService;

    @GetMapping("/list")
    public ResponseEntity<ListPaginationResponse<PlanResponse>> getListPlan(
            @RequestParam(required = false) Long termId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {

        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

            // Handling query
            if (query == null) {
                query = "";
            }

            // Get data
            List<FinancialPlan> plans = planService.getPlanWithPagination(query, termId, departmentId, statusId, pageInt, sizeInt, sortBy, sortType);

            // Response
            ListPaginationResponse<PlanResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (plans != null) {
                // Count total record
                count = planService.countDistinct(query, termId, departmentId, statusId);

                for (FinancialPlan plan : plans) {
                    //mapperToPlanResponse
                    response.getData().add(new ListPlanResponseMapperImpl().mapToPlanResponseMapper(plan));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @GetMapping("expenses")
    public ResponseEntity<ListPaginationResponse<ExpenseResponse>> getListExpense(
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer costTypeId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListPaginationResponse<ExpenseResponse> listPaginationResponse = new ListPaginationResponse<>();
        listPaginationResponse.setData(List.of(
                ExpenseResponse.builder()
                        .expenseId(1L)
                        .name("Promotion event")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(1L)
                                .name("Direct cost").build())
                        .unitPrice(BigDecimal.valueOf(15000000))
                        .amount(3)
                        .projectName("RECT")
                        .supplierName("Hong Ha")
                        .pic("HongHD9")
                        .notes("Approximate")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval").build())
                        .build(),
                ExpenseResponse.builder()
                        .expenseId(2L)
                        .name("Social media")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(1L)
                                .name("Direct cost").build())
                        .unitPrice(BigDecimal.valueOf(15000000))
                        .amount(1)
                        .projectName("IN22")
                        .supplierName("Hong Ha")
                        .pic("HongHD9")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(2L)
                                .name("Waiting for approval").build())
                        .build(),
                ExpenseResponse.builder()
                        .expenseId(3L)
                        .name("Office supplier")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(2L)
                                .name("Adminstration").build())
                        .unitPrice(BigDecimal.valueOf(5000000))
                        .amount(2)
                        .projectName("CAM1")
                        .supplierName("TuNM")
                        .pic("TuanVV")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval").build())
                        .build()
        ));

        listPaginationResponse.setPagination(Pagination.builder()
                .totalRecords(2222)
                .page(10)
                .limitRecordsPerPage(33)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listPaginationResponse);
    }

    @GetMapping("/detail")
    public ResponseEntity<PlanDetailResponse> getPlanDetail(
            @Valid @RequestBody PlanDetailBody planDetailBody
    ) throws Exception {
        try {
            // Get data
            PlanDetailResult plan = planService.getPlanDetailByPlanId(planDetailBody.getPlanId());

            // Response
            PlanDetailResponse response;

            if (plan != null) {
                // Mapping to PlanDetail Response
                response = new PlanDetailMapperImpl().mapToPlanDetailResponseMapping(plan);
                response.setVersion(planService.getPlanVersionById(planDetailBody.getPlanId()));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> generateXlsxReport(
            @RequestParam Integer planId
    ) throws Exception {
        String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
        FileInputStream file = new FileInputStream(fileLocation);
        XSSFWorkbook wb = new XSSFWorkbook(file);

        Sheet sheet = wb.getSheet("Expense");

        String[][] tableData = {
                {"Code Expense 1", "31/05/2024", "Financial plan December Q3 2021", "BU 01", "Promotion event", "Direction cost", "15000000", "3", "45000000", "RECT", "Hong Ha", "HongHD9", "Approximate", "Waiting for approximate"},
                {"Code Expense 2", "31/05/2024", "Financial plan December Q3 2021", "BU 02", "Social media", "Direction cost", "1000000", "3", "3000000", "CAM1", "Internal", "LanNT12", "", "Approved"},
                {"Code Expense 3", "31/05/2024", "Financial plan December Q3 2021", "BU 01", "Office supplies", "Administration cost", "1000000", "5", "5000000", "RECT1", "Internal", "AnhMN2", "", "Approved"},
                {"Code Expense 4", "31/05/2024", "Financial plan December Q3 2021", "BU 02", "Internal training", "Operating cost", "1000000", "4", "4000000", "CAM2", "Internal", "LanNT12", "", "Waiting for approval"}
        };

        Row row = null;
        Cell cell = null;

        int rowPosition = 2;
        int colPosition = 0;

        for (int i = 0; i < tableData.length; i++) {
            row = sheet.getRow(i + rowPosition);

            for (int j = 0; j < tableData[0].length; j++) {
                cell = row.getCell(j + colPosition);

                cell.setCellValue(tableData[i][j]);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();
        byte[] report = out.toByteArray();

        String outFileName = "report.xlsx";

        return createResponseEntity(report, outFileName);
    }

    private ResponseEntity<byte[]> createResponseEntity(
            byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(report);
    }

    @GetMapping("/plan-status")
    public ResponseEntity<ListResponse<StatusResponse>> getListStatus() {
        try {
            // Get data
            List<PlanStatus> costTypes = planService.getListPlanStatus();

            // Response
            ListResponse<StatusResponse> responses = new ListResponse<>();

            if (costTypes != null) {

                // Mapping to CostTypeResponse
                responses.setData(costTypes.stream().map(status -> {
                    return new PlanStatusMapperImpl().mapToStatusResponseMapping(status);
                }).toList());
            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(responses);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @GetMapping("versions")
    public ResponseEntity<ListPaginationResponse<VersionResponse>> getListVersion(
            @RequestParam Integer planId,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListPaginationResponse<VersionResponse> listPaginationResponse = new ListPaginationResponse<>();
        listPaginationResponse.setData(List.of(
                VersionResponse.builder()
                        .version("v1")
                        .publishedDate(LocalDate.of(2024, 4, 10))
                        .uploadedBy(UserResponse.builder()
                                .userId(1L)
                                .username("Anhln").build()).build(),
                VersionResponse.builder()
                        .version("v2")
                        .publishedDate(LocalDate.now())
                        .uploadedBy(UserResponse.builder()
                                .userId(1L)
                                .username("Anhln").build()).build(),
                VersionResponse.builder()
                        .version("v3")
                        .publishedDate(LocalDate.now())
                        .uploadedBy(UserResponse.builder()
                                .userId(1L)
                                .username("Anhln").build()).build()
        ));

        listPaginationResponse.setPagination(Pagination.builder()
                .totalRecords(2222)
                .page(10)
                .limitRecordsPerPage(33)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listPaginationResponse);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<String> deletePlan(
            @Valid @RequestBody DeletePlanBody planBody, BindingResult bindingResult) {
        try {
            FinancialPlan deletedPlan = planService.deletePlan(planBody.getPlanId());

            if (deletedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok("Delete successful plan id: " + deletedPlan.getId());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete plan");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found any plan have id = " + planBody.getPlanId());
        }

    }

    @PutMapping("/re-upload")
    private ResponseEntity<ListBody<ReUploadExpenseBody>> reUploadPlan(
            @Valid @RequestBody ListBody<ReUploadExpenseBody> expenseListBody
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(expenseListBody);
    }

    @PostMapping("/create")
    public ResponseEntity<String> confirmExpenses(
            @Valid @RequestBody NewPlanBody planBody, BindingResult bindingResult) throws Exception {
        try {
            // Get user detail
            UserDetail userDetail = planService.getUserDetail();

            // Get term
            Term term = planService.getTermById(planBody.getTermId());

            // Mapping to planBody to FinancialPlan
            FinancialPlan plan = new CreatePlanMapperImpl().mapPlanBodyToPlanMapping(planBody, userDetail.getDepartmentId(), (long) UserHelper.getUserId(), term.getName());

            // Save plan
            FinancialPlan savedPlan = planService.creatPlan(plan, term);

            if (savedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Create successful");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to create plan");
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This term already have plan of this department");
        } catch (InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plan due date of this term was expired");
        }
    }

    @PutMapping("/expense-approval")
    public ResponseEntity<String> approvalExpenses(
            @Valid @RequestBody ApprovalExpenseBody body, BindingResult bindingResult) throws Exception {
        try {

            planService.approvalExpenses(body.getListExpenseId());

            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

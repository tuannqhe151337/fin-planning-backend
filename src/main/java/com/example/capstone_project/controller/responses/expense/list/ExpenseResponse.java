package com.example.capstone_project.controller.responses.expense.list;

import com.example.capstone_project.controller.responses.expense.CurrencyResponse;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.PicResponse;
import com.example.capstone_project.controller.responses.expense.ProjectResponse;
import com.example.capstone_project.controller.responses.expense.SupplierResponse;
import com.example.capstone_project.controller.responses.expense.ApprovedByResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Long expenseId;
    private String expenseCode;
    private String name;
    private CostTypeResponse costType;
    private BigDecimal unitPrice;
    private Integer amount;
    private ProjectResponse project;
    private SupplierResponse supplier;
    private PicResponse pic;
    private String notes;
    private StatusResponse status;
    private CurrencyResponse currency;
    private ApprovedByResponse approvedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

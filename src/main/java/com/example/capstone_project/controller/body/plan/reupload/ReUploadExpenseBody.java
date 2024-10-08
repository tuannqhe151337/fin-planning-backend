package com.example.capstone_project.controller.body.plan.reupload;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReUploadExpenseBody {
    private Long expenseId;

    @NotEmpty(message = "Expense name can't be empty.")
    @Size(max = 100, message = "Expense name must be less than 100 characters")
    private String expenseName;

    @NotNull(message = "Expense cost type id can't be null.")
    private Long costTypeId;

    @NotNull(message = "Unit price can't be null.")
    @Min(value = 0, message = "Unit price can't be negative")
    private BigDecimal unitPrice;

    @NotNull(message = "Amount can't be null.")
    @Min(value = 0, message = "Amount can't be negative")
    private Integer amount;

    @NotNull(message = "Project name can't be empty.")
    private Long projectId;

    @NotNull(message = "Supplier name can't be empty.")
    private Long supplierId;

    @NotNull(message = "PIC can't be empty.")
    private Long picId;

    @Size(max = 200, message = "Notes must be less than 200 characters")
    private String notes;

    @NotNull(message = "Currency id can't be empty.")
    private Long currencyId;
}

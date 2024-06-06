package com.example.capstone_project.controller.responses.term.get;

import com.example.capstone_project.entity.TermDuration;
import com.example.capstone_project.entity.TermStatus;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TermDetailResponse {
    @NotEmpty(message = "ID cannot be empty")
    private long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Duration cannot be empty")
    @Column(name = "duration")
    private TermDuration duration ;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End due date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Plan due date cannot be null")
    @Column(name = "plan_due_date")
    @Future(message = "Plan due date must be in the future")
    private LocalDateTime planDueDate;

    @NotNull(message = "Status cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private TermStatusResponse status;

    @AssertTrue(message = "Plan due date must be before end date")
    private boolean isPlanDueDateBeforeEndDate() {
        if (planDueDate == null || endDate == null) {
            return true; // Để @NotNull xử lý null check
        }
        return planDueDate.isBefore(endDate);
    }
}

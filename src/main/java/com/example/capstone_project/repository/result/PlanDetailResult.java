package com.example.capstone_project.repository.result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PlanDetailResult {
     Long getPlanId();
     String getName();
     Long getTermId();
     String getTermName();
     LocalDateTime getTermStartDate();
     LocalDateTime getTermEndDate();
     LocalDateTime getTermReuploadStartDate();
     LocalDateTime getTermReuploadEndDate();
     LocalDateTime getTermFinalEndTermDate();
     LocalDateTime getCreatedAt();
     Long getDepartmentId();
     String getDepartmentName();
     Long getUserId();
     String getUsername();
}

package com.example.capstone_project.repository.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReportDetailResult {
    Long getReportId();

    String getName();

    Long getTermId();

    String getTermName();

    Long getStatusId();

    String getStatusCode();

    String getStatusName();

    LocalDateTime getCreatedAt();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    boolean isAllowReupload();

    LocalDateTime getReuploadStartDate();

    LocalDateTime getReuploadEndDate();

    LocalDateTime getFinalEndTermDate();
}
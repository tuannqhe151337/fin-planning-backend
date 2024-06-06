package com.example.capstone_project.controller.responses.plan.version;

import com.example.capstone_project.controller.responses.plan.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class VersionResponse {
    private String version;
    private LocalDate publishedDate;
    private UserResponse uploadedBy;
}
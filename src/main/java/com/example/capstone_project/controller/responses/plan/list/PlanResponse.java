package com.example.capstone_project.controller.responses.plan.list;

import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanResponse {
    private Long planId;
    private String name;
    private TermResponse term;
    private DepartmentResponse department;
    private String version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.example.capstone_project.controller.responses.userManagement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {
    private long id;
    private String name;
}
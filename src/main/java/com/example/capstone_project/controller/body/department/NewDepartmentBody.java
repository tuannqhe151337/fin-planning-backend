package com.example.capstone_project.controller.body.department;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewDepartmentBody {
    @NotEmpty(message = "New department name can't be empty")
    private String departmentName;
}

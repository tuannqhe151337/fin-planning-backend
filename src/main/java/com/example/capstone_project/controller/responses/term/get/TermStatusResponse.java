package com.example.capstone_project.controller.responses.term.get;

import com.example.capstone_project.utils.enums.TermCode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TermStatusResponse {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Code cannot be empty")
    @Enumerated(EnumType.STRING)
    private TermCode code;

    private Boolean isDelete;
}

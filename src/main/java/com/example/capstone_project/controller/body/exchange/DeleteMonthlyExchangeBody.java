package com.example.capstone_project.controller.body.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteMonthlyExchangeBody {
    @NotNull(message = "Month can not be emptied")
    private String month;
}

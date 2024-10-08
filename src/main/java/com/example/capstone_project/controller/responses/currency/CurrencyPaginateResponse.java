package com.example.capstone_project.controller.responses.currency;

import com.example.capstone_project.utils.enums.Affix;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyPaginateResponse {
    private Long currencyId;
    private String name;
    private String symbol;
    private Affix affix;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

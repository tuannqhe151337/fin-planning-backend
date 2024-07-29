package com.example.capstone_project.controller.responses.term.paginate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {
    private long statusId;
    private String code;
    private String name;
}

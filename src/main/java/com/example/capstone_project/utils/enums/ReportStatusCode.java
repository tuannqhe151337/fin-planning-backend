package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum ReportStatusCode {
    NEW("New"),
    WAITING_FOR_APPROVAL("Waiting for approval"),
    REVIEWED("Reviewed"),
    CLOSED("Closed");

    private final String value;
    ReportStatusCode(String value) {
        this.value = value;
    }
}

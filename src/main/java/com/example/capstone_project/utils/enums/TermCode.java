package com.example.capstone_project.utils.enums;

public enum TermCode {

    IN_PROGRESS("IN PROGRESS"),
    NEW("NEW"),
    CLOSED("CLOSED");

    private String value;

    TermCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

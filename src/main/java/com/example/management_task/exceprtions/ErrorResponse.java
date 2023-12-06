package com.example.management_task.exceprtions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private List<String> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }
    public ErrorResponse(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
}


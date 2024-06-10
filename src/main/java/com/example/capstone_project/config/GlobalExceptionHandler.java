package com.example.capstone_project.config;

import com.example.capstone_project.controller.responses.ExceptionResponse;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ResponseEntity<List<ExceptionResponse>> handleConstraintViolationException(Exception ex, WebRequest request) {
            String exceptionMessage = ex.getMessage();
            List<ExceptionResponse> exceptionResponseList = this.parseMessageToListExceptionResponse(exceptionMessage);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseList);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ResponseEntity<List<ExceptionResponse>> handleHttpMessageNotReadableException(Exception ex, WebRequest request) {
            String exceptionMessage = ex.getMessage();
            List<ExceptionResponse> exceptionResponseList = this.parseMessageToListExceptionResponse(exceptionMessage);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseList);
        }

        private List<ExceptionResponse> parseMessageToListExceptionResponse(String errorMessage) {
            String[] errors = errorMessage.split(",");
            List<ExceptionResponse> exceptionResponseList = new ArrayList<>();
            for (String s : errors) {
                String[] parts = s.trim().split(": ");
                String fieldName = parts[0].substring(parts[0].lastIndexOf(".") + 1);
                String errorMessageText = parts[1];
                ExceptionResponse exception = ExceptionResponse.builder()
                        .field(fieldName).message(errorMessageText)
                        .build();
                exceptionResponseList.add(exception);
            }

            return exceptionResponseList;
        }
    }


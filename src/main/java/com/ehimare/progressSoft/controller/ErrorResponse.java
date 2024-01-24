package com.ehimare.progressSoft.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private String errorMessage;
    private HttpStatus statusCode;
}

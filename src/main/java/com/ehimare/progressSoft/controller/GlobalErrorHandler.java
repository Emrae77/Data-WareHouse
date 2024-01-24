package com.ehimare.progressSoft.controller;

import com.ehimare.progressSoft.exception.DealNotSavedException;
import com.ehimare.progressSoft.exception.ResourceNotFound;
import com.ehimare.progressSoft.exception.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    private ErrorResponse createErrorResponse(final HttpStatus statusCode, final String errorMessage) {
        final ErrorResponse response = new ErrorResponse();
        response.setStatusCode(statusCode);
        response.setErrorMessage(errorMessage);
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception ex) {
        final ErrorResponse response = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({DealNotSavedException.class, ValidatorException.class})
    public ResponseEntity<ErrorResponse> handleDealNotSaved(final Exception ex) {
        final ErrorResponse response = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(final ResourceNotFound resourceNotFound) {
        final ErrorResponse response = createErrorResponse(HttpStatus.NOT_FOUND, resourceNotFound.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

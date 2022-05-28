package com.github.manerajona.auth.exception.handlers;

import com.github.manerajona.auth.exception.error.ErrorCode;
import com.github.manerajona.auth.exception.error.ErrorDetails;
import com.github.manerajona.auth.exception.error.ErrorLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    ResponseEntity<?> handleBindException(BindException ex) {
        List<?> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ErrorDetails.builder()
                        .code(ErrorCode.INVALID_FIELD_VALUE)
                        .detail(fieldError.getDefaultMessage())
                        .field(fieldError.getField())
                        .location(ErrorLocation.BODY)
                        .build()
                ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

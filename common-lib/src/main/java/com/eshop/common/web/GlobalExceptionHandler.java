package com.eshop.common.web;

import com.eshop.common.api.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger Log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiError.FieldErrorItem> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ApiError.FieldErrorItem(err.getField(), err.getDefaultMessage()))
                .toList();

        return new ApiError(
                "about:blank",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "One or more fields are invalid",
                request.getRequestURI(),
                "VALIDATION_ERROR",
                request.getHeader("X-Correlation-Id"),
                Instant.now(),
                fieldErrors
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        Log.error("handleGeneric = " + ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal Server Error");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}

package com.migros.courier_tracking.adapter.in.web.exception;

import com.migros.courier_tracking.adapter.in.web.dto.common.ApiError;
import com.migros.courier_tracking.adapter.in.web.dto.common.ApiResponse;
import com.migros.courier_tracking.domain.exception.ErrorCode;
import com.migros.courier_tracking.domain.exception.base.BusinessException;
import com.migros.courier_tracking.domain.exception.base.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {

        log.warn("Resource not found: {} - {}", ex.getErrorCode(), ex.getMessage());

        ApiError error = ApiError.of(ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        ApiResponse<Void> response = ApiResponse.error("Resource not found", error);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        log.warn("Validation errors: {}", validationErrors);

        ApiError error = ApiError.withValidation(
                ErrorCode.VALIDATION_ERROR.getCode(),
                "Request validation failed",
                request.getRequestURI(),
                validationErrors
        );

        ApiResponse<Void> response = ApiResponse.error("Validation failed", error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        log.error("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());

        ApiError error = ApiError.of(ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        ApiResponse<Void> response = ApiResponse.error("Business error", error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred", ex);

        ApiError error = ApiError.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );

        ApiResponse<Void> response = ApiResponse.error("Internal server error", error);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

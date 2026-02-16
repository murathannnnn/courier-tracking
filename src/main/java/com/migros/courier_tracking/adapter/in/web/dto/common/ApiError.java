package com.migros.courier_tracking.adapter.in.web.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final String code;
    private final String message;
    private final String path;
    private final Map<String, String> validationErrors;
    private final List<String> details;

    public static ApiError of(String code, String message, String path) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .path(path)
                .build();
    }

    public static ApiError withValidation(String code, String message, String path,
                                          Map<String, String> validationErrors) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .path(path)
                .validationErrors(validationErrors)
                .build();
    }
}

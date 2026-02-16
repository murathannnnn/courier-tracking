package com.migros.courier_tracking.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Generic errors (1xxx)
    INTERNAL_SERVER_ERROR("ERR-1000", "An unexpected error occurred"),
    VALIDATION_ERROR("ERR-1001", "Validation failed"),

    // Courier errors (2xxx)
    COURIER_NOT_FOUND("ERR-2001", "Courier not found with id: %s"),

    // Store errors (3xxx)
    STORE_NOT_FOUND("ERR-3001", "Store not found: %s"),

    // Location errors (4xxx)
    INVALID_LOCATION("ERR-4001", "Invalid location: %s"),
    INVALID_COORDINATES("ERR-4002", "Invalid coordinates: latitude=%s, longitude=%s");

    private final String code;
    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}

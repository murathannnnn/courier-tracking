package com.migros.courier_tracking.domain.exception.base;

public abstract class NotFoundException extends BusinessException {

    protected NotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    protected NotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}

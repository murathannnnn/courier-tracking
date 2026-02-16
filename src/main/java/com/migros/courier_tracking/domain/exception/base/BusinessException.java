package com.migros.courier_tracking.domain.exception.base;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final String errorCode;
    private final Object[] args;

    protected BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = null;
    }

    protected BusinessException(String errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    protected BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = null;
    }
}

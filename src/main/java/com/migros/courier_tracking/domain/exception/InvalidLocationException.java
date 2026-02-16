package com.migros.courier_tracking.domain.exception;

import com.migros.courier_tracking.domain.exception.base.BusinessException;

public class InvalidLocationException extends BusinessException {

    public InvalidLocationException(String message) {
        super(ErrorCode.INVALID_LOCATION.getCode(), message);
    }

    public InvalidLocationException(double latitude, double longitude) {
        super(
                ErrorCode.INVALID_COORDINATES.getCode(),
                ErrorCode.INVALID_COORDINATES.formatMessage(latitude, longitude),
                latitude,
                longitude
        );
    }
}

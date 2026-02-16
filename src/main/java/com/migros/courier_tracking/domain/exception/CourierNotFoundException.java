package com.migros.courier_tracking.domain.exception;

import com.migros.courier_tracking.domain.exception.base.NotFoundException;

public class CourierNotFoundException extends NotFoundException {

    public CourierNotFoundException(Long courierId) {
        super(
                ErrorCode.COURIER_NOT_FOUND.getCode(),
                ErrorCode.COURIER_NOT_FOUND.formatMessage(courierId),
                courierId
        );
    }
}

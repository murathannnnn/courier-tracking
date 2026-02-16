package com.migros.courier_tracking.domain.exception;

import com.migros.courier_tracking.domain.exception.base.NotFoundException;

public class StoreNotFoundException extends NotFoundException {

    public StoreNotFoundException(String storeName) {
        super(
                ErrorCode.STORE_NOT_FOUND.getCode(),
                ErrorCode.STORE_NOT_FOUND.formatMessage(storeName),
                storeName
        );
    }
}

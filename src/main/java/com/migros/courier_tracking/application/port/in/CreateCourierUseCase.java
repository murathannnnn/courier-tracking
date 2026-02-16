package com.migros.courier_tracking.application.port.in;

import com.migros.courier_tracking.domain.model.Courier;

public interface CreateCourierUseCase {
    Courier createCourier(String name);
}
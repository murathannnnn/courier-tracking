package com.migros.courier_tracking.application.port.out;

import com.migros.courier_tracking.domain.model.Courier;

import java.util.Optional;

public interface CourierRepository {

    Optional<Courier> findById(Long courierId);

    Courier save(Courier courier);

    void updateTotalDistance(Long courierId, double distance);
}

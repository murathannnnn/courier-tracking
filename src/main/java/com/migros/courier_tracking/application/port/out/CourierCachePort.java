package com.migros.courier_tracking.application.port.out;

import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.Location;

import java.util.Optional;

public interface CourierCachePort {

    Optional<Courier> getCourier(Long courierId);

    void saveCourier(Courier courier);

    void evictCourier(Long courierId);

    Optional<Location> getLastLocation(Long courierId);

    void saveLastLocation(Location location);
}

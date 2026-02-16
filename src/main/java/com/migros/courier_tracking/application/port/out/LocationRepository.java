package com.migros.courier_tracking.application.port.out;

import com.migros.courier_tracking.domain.model.Location;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LocationRepository {

    Location save(Location location);

    Optional<Location> findLastLocationByCourierId(Long courierId);

    List<Location> findAllByCourierId(Long courierId);

    Boolean existsByCourierIdAndLatitudeAndLongitudeAndTimestamp(
            Long courierId, Double latitude, Double longitude, Instant timestamp);
}

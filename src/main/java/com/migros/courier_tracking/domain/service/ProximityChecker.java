package com.migros.courier_tracking.domain.service;

import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.model.Store;
import com.migros.courier_tracking.domain.service.strategy.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProximityChecker {

    private static final double STORE_RADIUS_METERS = 100.0;

    private final DistanceCalculator distanceCalculator;

    public boolean isWithinStoreRadius(Location location, Store store) {
        Location storeLocation = Location.builder()
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .build();

        // Calculate geographical distance using Haversine formula
        double distance = distanceCalculator.calculate(location, storeLocation);

        // Check if within 100-meter radius
        boolean withinRadius = distance <= STORE_RADIUS_METERS;

        // Log courier is inside store radius
        if (withinRadius) {
            log.trace("Courier within {}m of {} (distance: {}m)",
                    STORE_RADIUS_METERS, store.getName(), distance);
        }

        return withinRadius;
    }
    public double getStoreRadiusMeters() {
        return STORE_RADIUS_METERS;
    }
}

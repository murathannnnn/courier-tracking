package com.migros.courier_tracking.application.service;

import com.migros.courier_tracking.application.port.in.TrackLocationUseCase;
import com.migros.courier_tracking.application.port.out.CourierRepository;
import com.migros.courier_tracking.application.port.out.LocationEventPublisher;
import com.migros.courier_tracking.application.port.out.LocationRepository;
import com.migros.courier_tracking.domain.exception.CourierNotFoundException;
import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.service.strategy.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingService implements TrackLocationUseCase {

    private final LocationRepository locationRepository;
    private final CourierRepository courierRepository;
    private final DistanceCalculator distanceCalculator;
    private final LocationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void trackLocation(Location location) {
        log.debug("Location update received for courier {}", location.getCourierId());

        Courier courier = courierRepository.findById(location.getCourierId())
                .orElseThrow(() -> {
                    log.error("Location update rejected. Courier {} not found!", location.getCourierId());
                    return new CourierNotFoundException(location.getCourierId());
                });

        // Idempotency check: If the same location update is received again, skip processing
        if (isDuplicateRequest(location)) {
            return;
        }

        Location previousLocation = locationRepository.findLastLocationByCourierId(location.getCourierId())
                .orElse(null);

        Location savedLocation = locationRepository.save(location);

        if (previousLocation != null) {
            if (isSameLocation(previousLocation, savedLocation)) {
                log.debug("Courier {} is at the same location, skipping distance calculation", location.getCourierId());
                eventPublisher.publishLocationUpdate(savedLocation);
                return;
            }

            double distance = distanceCalculator.calculate(previousLocation, savedLocation);
            updateCourierDistance(courier, distance);

            log.info("Updated total distance for courier {}", location.getCourierId());
        }

        eventPublisher.publishLocationUpdate(savedLocation);
    }

    private void updateCourierDistance(Courier courier, double distance) {
        double previousTotal = courier.getTotalDistance();
        courier.addDistance(distance);

        courierRepository.save(courier);

        log.debug("Distance accumulation for courier {}: {} + {} = {} meters",
                courier.getId(), previousTotal, distance, courier.getTotalDistance());
    }

    private boolean isSameLocation(Location loc1, Location loc2) {
        return Double.compare(loc1.getLatitude(), loc2.getLatitude()) == 0
                && Double.compare(loc1.getLongitude(), loc2.getLongitude()) == 0;
    }

    private boolean isDuplicateRequest(Location location) {
        boolean exists = locationRepository.existsByCourierIdAndLatitudeAndLongitudeAndTimestamp(
                location.getCourierId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getTimestamp()
        );

        if (exists) {
            log.warn("Duplicate location update detected and skipped for courier {} at {}",
                    location.getCourierId(), location.getTimestamp());
        }
        return exists;
    }
}
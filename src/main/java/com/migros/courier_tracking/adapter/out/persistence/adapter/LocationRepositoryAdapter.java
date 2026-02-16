package com.migros.courier_tracking.adapter.out.persistence.adapter;

import com.migros.courier_tracking.adapter.out.persistence.mapper.LocationEntityMapper;
import com.migros.courier_tracking.adapter.out.persistence.repository.JpaLocationRepository;
import com.migros.courier_tracking.application.port.out.CourierCachePort;
import com.migros.courier_tracking.application.port.out.LocationRepository;
import com.migros.courier_tracking.domain.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationRepositoryAdapter implements LocationRepository {

    private final JpaLocationRepository jpaLocationRepository;
    private final LocationEntityMapper locationEntityMapper;
    private final CourierCachePort cachePort;

    @Override
    public Location save(Location location) {
        log.debug("Saving location for courier {} with cache update", location.getCourierId());

        //Save to database
        var entity = locationEntityMapper.toEntity(location);
        var savedEntity = jpaLocationRepository.save(entity);
        Location savedLocation = locationEntityMapper.toDomain(savedEntity);

        //Update cache
        log.debug("Updating last location cache for courier {}", location.getCourierId());
        cachePort.saveLastLocation(savedLocation);

        return savedLocation;
    }

    @Override
    public Optional<Location> findLastLocationByCourierId(Long courierId) {
        log.debug("Finding last location for courier {} with cache-aside", courierId);

        //Check cache first
        Optional<Location> cachedLocation = cachePort.getLastLocation(courierId);
        if (cachedLocation.isPresent()) {
            log.debug("Cache HIT: Returning last location for courier {} from cache", courierId);
            return cachedLocation;
        }

        //Cache miss
        log.debug("Cache MISS: Loading last location for courier {} from database", courierId);
        Optional<Location> locationFromDb = jpaLocationRepository.findLastLocationByCourierId(courierId)
                .map(locationEntityMapper::toDomain);

        //Write to cache for future requests
        locationFromDb.ifPresent(location -> {
            log.debug("Writing last location to cache for courier {}", courierId);
            cachePort.saveLastLocation(location);
        });

        return locationFromDb;
    }

    @Override
    public List<Location> findAllByCourierId(Long courierId) {
        log.debug("Finding all locations for courier {} (no caching)", courierId);

        return jpaLocationRepository.findAllByCourierIdOrderByTimestampDesc(courierId).stream()
                .map(locationEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByCourierIdAndLatitudeAndLongitudeAndTimestamp(
            Long courierId, Double latitude, Double longitude, Instant timestamp) {
        return jpaLocationRepository.existsByCourierIdAndLatitudeAndLongitudeAndTimestamp(
                courierId, latitude, longitude, timestamp);
    }
}

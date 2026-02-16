package com.migros.courier_tracking.adapter.out.persistence.adapter;

import com.migros.courier_tracking.adapter.out.persistence.mapper.CourierEntityMapper;
import com.migros.courier_tracking.adapter.out.persistence.repository.JpaCourierRepository;
import com.migros.courier_tracking.application.port.out.CourierCachePort;
import com.migros.courier_tracking.application.port.out.CourierRepository;
import com.migros.courier_tracking.domain.model.Courier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourierRepositoryAdapter implements CourierRepository {

    private final JpaCourierRepository jpaCourierRepository;
    private final CourierEntityMapper courierEntityMapper;
    private final CourierCachePort cachePort;

    @Override
    public Optional<Courier> findById(Long courierId) {
        log.debug("Finding courier {} with cache-aside pattern", courierId);

        //Check cache first
        Optional<Courier> cachedCourier = cachePort.getCourier(courierId);
        if (cachedCourier.isPresent()) {
            log.debug("Cache HIT: Returning courier {} from cache", courierId);
            return cachedCourier;
        }

        //Cache miss - load from database
        log.debug("Cache MISS: Loading courier {} from database", courierId);
        Optional<Courier> courierFromDb = jpaCourierRepository.findById(courierId)
                .map(courierEntityMapper::toDomain);

        //If found in DB, write to cache for next time
        courierFromDb.ifPresent(courier -> {
            log.debug("Writing courier {} to cache after DB load", courierId);
            cachePort.saveCourier(courier);
        });

        return courierFromDb;
    }

    @Override
    public Courier save(Courier courier) {
        log.debug("Saving courier {} with cache update", courier.getId());

        //Save to database first (source of truth)
        var entity = courierEntityMapper.toEntity(courier);
        var savedEntity = jpaCourierRepository.save(entity);
        Courier savedCourier = courierEntityMapper.toDomain(savedEntity);

        //Update cache with fresh data
        log.debug("Updating cache for courier {} after save", savedCourier.getId());
        cachePort.saveCourier(savedCourier);

        return savedCourier;
    }

    @Override
    public void updateTotalDistance(Long courierId, double distance) {
        log.debug("Updating total distance for courier {} and evicting cache", courierId);

        jpaCourierRepository.findById(courierId).ifPresent(entity -> {
            //Update database
            entity.setTotalDistance(entity.getTotalDistance() + distance);
            jpaCourierRepository.save(entity);

            //Evict cache to force refresh on next read
            log.debug("Evicting courier {} from cache after distance update", courierId);
            cachePort.evictCourier(courierId);
        });
    }
}

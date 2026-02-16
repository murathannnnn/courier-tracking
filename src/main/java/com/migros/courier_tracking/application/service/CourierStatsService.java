package com.migros.courier_tracking.application.service;

import com.migros.courier_tracking.application.port.in.GetCourierStatsUseCase;
import com.migros.courier_tracking.application.port.out.CourierRepository;
import com.migros.courier_tracking.application.port.out.StoreEntryRepository;
import com.migros.courier_tracking.domain.exception.CourierNotFoundException;
import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.StoreEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierStatsService implements GetCourierStatsUseCase {

    private final CourierRepository courierRepository;
    private final StoreEntryRepository storeEntryRepository;
    @Override
    @Transactional(readOnly = true)
    public double getTotalTravelDistance(Long courierId) {
        log.debug("Querying total travel distance for courier {}", courierId);

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> {
                    log.warn("Courier {} not found when querying distance", courierId);
                    return new CourierNotFoundException(courierId);
                });

        double totalDistance = courier.getTotalDistance();

        log.debug("Total travel distance for courier {}: {} meters",
                courierId, totalDistance);

        return totalDistance;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreEntry> getStoreEntries(Long courierId) {
        log.debug("Querying store entries for courier {}", courierId);

        // Verify courier exists
        courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException(courierId));

        List<StoreEntry> entries = storeEntryRepository.findAllByCourierId(courierId);

        log.debug("Found {} store entries for courier {}", entries.size(), courierId);

        return entries;
    }
}

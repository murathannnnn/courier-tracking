package com.migros.courier_tracking.domain.service;

import com.migros.courier_tracking.application.port.out.LastVisitRegistry;
import com.migros.courier_tracking.application.port.out.StoreEntryRepository;
import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.model.Store;
import com.migros.courier_tracking.domain.model.StoreEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreEntryProcessorImpl implements StoreEntryProcessor{
    private final StoreEntryRepository storeEntryRepository;
    private final LastVisitRegistry lastVisitRegistry;
    private final ProximityChecker proximityChecker;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processStoreEntry(Location location, Store store) {
        if (!proximityChecker.isWithinStoreRadius(location, store)) {
            log.trace("Courier {} is outside 100m radius of store {}",
                    location.getCourierId(), store.getName());
            return; //If outside radius, do NOT log
        }

        log.debug("Courier {} is within 100m of store {}",
                location.getCourierId(), store.getName());

        if (!lastVisitRegistry.canLogNewEntry(location.getCourierId(), store.getName(), location.getTimestamp())) {
            log.debug("Courier {} visited {} within last minute, NOT logging (preventing duplicate)",
                    location.getCourierId(), store.getName());
            return;
        }

        log.debug("More than 1 minute since last visit for courier {} at store {}",
                location.getCourierId(), store.getName());

        StoreEntry entry = StoreEntry.builder()
                .courierId(location.getCourierId())
                .storeName(store.getName())
                .entryTime(location.getTimestamp())
                .build();

        storeEntryRepository.save(entry);
        log.info("Persisted store entry to database - Courier {} entered store {} at {}",
                location.getCourierId(), store.getName(), location.getTimestamp());

        lastVisitRegistry.saveLastVisit(location.getCourierId(), store.getName(), location.getTimestamp());

        log.debug("Updated last visit timestamp in Redis for courier {} at store {}",
                location.getCourierId(), store.getName());
    }
}

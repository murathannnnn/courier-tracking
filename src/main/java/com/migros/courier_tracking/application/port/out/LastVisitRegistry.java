package com.migros.courier_tracking.application.port.out;

import java.time.Instant;
import java.util.Optional;

public interface LastVisitRegistry {

    void saveLastVisit(Long courierId, String storeName, Instant timestamp);

    Optional<Instant> getLastVisit(Long courierId, String storeName);

    boolean canLogNewEntry(Long courierId, String storeName, Instant currentTime);
}

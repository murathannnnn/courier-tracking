package com.migros.courier_tracking.application.port.out;

import com.migros.courier_tracking.domain.model.StoreEntry;

import java.util.List;

public interface StoreEntryRepository {

    StoreEntry save(StoreEntry storeEntry);

    List<StoreEntry> findAllByCourierId(Long courierId);
}

package com.migros.courier_tracking.domain.service;

import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.model.Store;

public interface StoreEntryProcessor {
    void processStoreEntry(Location location, Store store);
}

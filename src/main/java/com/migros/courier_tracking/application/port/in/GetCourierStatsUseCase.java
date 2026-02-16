package com.migros.courier_tracking.application.port.in;

import com.migros.courier_tracking.domain.model.StoreEntry;

import java.util.List;

public interface GetCourierStatsUseCase {

    double getTotalTravelDistance(Long courierId);

    List<StoreEntry> getStoreEntries(Long courierId);
}

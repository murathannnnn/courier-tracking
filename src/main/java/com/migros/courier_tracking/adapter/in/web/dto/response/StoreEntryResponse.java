package com.migros.courier_tracking.adapter.in.web.dto.response;

import java.time.Instant;

public record StoreEntryResponse(
        Long courierId,
        String storeName,
        Instant entryTime
) {
}

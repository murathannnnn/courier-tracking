package com.migros.courier_tracking.adapter.in.web.mapper;

import com.migros.courier_tracking.adapter.in.web.dto.request.LocationUpdateRequest;
import com.migros.courier_tracking.adapter.in.web.dto.response.StoreEntryResponse;
import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.model.StoreEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationMapper {

    public Location toDomain(LocationUpdateRequest request) {
        return Location.builder()
                .courierId(request.courierId())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .timestamp(request.timestamp())
                .build();
    }

    public StoreEntryResponse toResponse(StoreEntry storeEntry) {
        return new StoreEntryResponse(
                storeEntry.getCourierId(),
                storeEntry.getStoreName(),
                storeEntry.getEntryTime()
        );
    }

    public List<StoreEntryResponse> toResponseList(List<StoreEntry> storeEntries) {
        return storeEntries.stream()
                .map(this::toResponse)
                .toList();
    }
}

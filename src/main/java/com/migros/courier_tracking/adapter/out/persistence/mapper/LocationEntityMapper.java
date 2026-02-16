package com.migros.courier_tracking.adapter.out.persistence.mapper;

import com.migros.courier_tracking.adapter.out.persistence.entity.LocationEntity;
import com.migros.courier_tracking.domain.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationEntityMapper {

    public Location toDomain(LocationEntity entity) {
        return Location.builder()
                .id(entity.getId())
                .courierId(entity.getCourierId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public LocationEntity toEntity(Location domain) {
        return LocationEntity.builder()
                .id(domain.getId())
                .courierId(domain.getCourierId())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .timestamp(domain.getTimestamp())
                .build();
    }
}

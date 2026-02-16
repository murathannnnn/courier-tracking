package com.migros.courier_tracking.adapter.out.persistence.mapper;

import com.migros.courier_tracking.adapter.out.persistence.entity.StoreEntity;
import com.migros.courier_tracking.domain.model.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreEntityMapper {

    public Store toDomain(StoreEntity entity) {
        return Store.builder()
                .id(entity.getId())
                .name(entity.getName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }

    public StoreEntity toEntity(Store domain) {
        return StoreEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .build();
    }
}

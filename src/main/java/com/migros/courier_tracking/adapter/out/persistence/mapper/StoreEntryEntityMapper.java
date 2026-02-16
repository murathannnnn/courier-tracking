package com.migros.courier_tracking.adapter.out.persistence.mapper;

import com.migros.courier_tracking.adapter.out.persistence.entity.StoreEntryEntity;
import com.migros.courier_tracking.domain.model.StoreEntry;
import org.springframework.stereotype.Component;

@Component
public class StoreEntryEntityMapper {

    public StoreEntry toDomain(StoreEntryEntity entity) {
        return StoreEntry.builder()
                .id(entity.getId())
                .courierId(entity.getCourierId())
                .storeName(entity.getStoreName())
                .entryTime(entity.getEntryTime())
                .build();
    }

    public StoreEntryEntity toEntity(StoreEntry domain) {
        return StoreEntryEntity.builder()
                .id(domain.getId())
                .courierId(domain.getCourierId())
                .storeName(domain.getStoreName())
                .entryTime(domain.getEntryTime())
                .build();
    }
}

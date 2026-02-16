package com.migros.courier_tracking.adapter.out.persistence.mapper;

import com.migros.courier_tracking.adapter.out.persistence.entity.CourierEntity;
import com.migros.courier_tracking.domain.model.Courier;
import org.springframework.stereotype.Component;

@Component
public class CourierEntityMapper {

    public Courier toDomain(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalDistance(entity.getTotalDistance())
                .build();
    }

    public CourierEntity toEntity(Courier domain) {
        return CourierEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .totalDistance(domain.getTotalDistance())
                .build();
    }
}

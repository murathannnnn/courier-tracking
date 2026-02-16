package com.migros.courier_tracking.adapter.in.web.dto.response;


import com.migros.courier_tracking.domain.model.Courier;

public record CreateCourierResponse(
        Long id,
        String name,
        Double totalDistance
) {
    public static CreateCourierResponse fromDomain(Courier courier) {
        return new CreateCourierResponse(
                courier.getId(),
                courier.getName(),
                courier.getTotalDistance()
        );
    }
}
package com.migros.courier_tracking.adapter.in.web.dto.response;

public record TotalDistanceResponse(
        Long courierId,
        Double totalDistance,
        String unit
) {
    public static TotalDistanceResponse of(Long courierId, Double distance) {
        return new TotalDistanceResponse(courierId, distance, "meters");
    }
}

package com.migros.courier_tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private Long id;
    private Long courierId;
    private double latitude;
    private double longitude;
    private Instant timestamp;
}
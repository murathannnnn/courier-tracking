package com.migros.courier_tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Courier {

    private Long id;
    private String name;
    private double totalDistance;

    public void addDistance(double distance) {
        this.totalDistance += distance;
    }
}


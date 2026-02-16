package com.migros.courier_tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    private Long id;
    private String name;
    private double latitude;
    private double longitude;
}

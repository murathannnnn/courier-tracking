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
public class StoreEntry {

    private Long id;
    private Long courierId;
    private String storeName;
    private Instant entryTime;
}

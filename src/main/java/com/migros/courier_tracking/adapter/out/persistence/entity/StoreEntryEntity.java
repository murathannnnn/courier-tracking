package com.migros.courier_tracking.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "store_entries", indexes = {
        @Index(name = "idx_courier_entry", columnList = "courier_id,entry_time")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "courier_id", nullable = false)
    private Long courierId;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "entry_time", nullable = false)
    private Instant entryTime;
}

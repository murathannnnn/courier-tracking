package com.migros.courier_tracking.application.service;

import com.migros.courier_tracking.application.port.out.CourierRepository;
import com.migros.courier_tracking.application.port.out.StoreEntryRepository;
import com.migros.courier_tracking.domain.exception.CourierNotFoundException;
import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.StoreEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@DisplayName("Courier Stats Service Tests")
@ExtendWith(MockitoExtension.class)
class CourierStatsServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private StoreEntryRepository storeEntryRepository;

    private CourierStatsService courierStatsService;

    @BeforeEach
    void setUp() {
        courierStatsService = new CourierStatsService(courierRepository, storeEntryRepository);
    }

    @Test
    @DisplayName("Should return total distance for existing courier")
    void shouldReturnTotalDistanceForExistingCourier() {
        Long courierId = 1L;
        Courier courier = Courier.builder()
                .id(courierId)
                .name("Test Courier")
                .totalDistance(1500.5)
                .build();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        double distance = courierStatsService.getTotalTravelDistance(courierId);

        assertThat(distance).isEqualTo(1500.5);
        verify(courierRepository).findById(courierId);
    }

    @Test
    @DisplayName("Should throw exception when courier not found for distance query")
    void shouldThrowExceptionWhenCourierNotFoundForDistance() {
        Long courierId = 999L;
        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierStatsService.getTotalTravelDistance(courierId))
                .isInstanceOf(CourierNotFoundException.class)
                .hasMessageContaining("999");

        verify(courierRepository).findById(courierId);
    }

    @Test
    @DisplayName("Should return store entries for existing courier")
    void shouldReturnStoreEntriesForExistingCourier() {
        Long courierId = 1L;
        Courier courier = Courier.builder()
                .id(courierId)
                .name("Test Courier")
                .totalDistance(1000.0)
                .build();

        List<StoreEntry> entries = List.of(
                StoreEntry.builder()
                        .id(1L)
                        .courierId(courierId)
                        .storeName("Ataşehir MMM Migros")
                        .entryTime(Instant.now())
                        .build(),
                StoreEntry.builder()
                        .id(2L)
                        .courierId(courierId)
                        .storeName("Novada MMM Migros")
                        .entryTime(Instant.now().plusSeconds(300))
                        .build()
        );

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(storeEntryRepository.findAllByCourierId(courierId)).thenReturn(entries);

        List<StoreEntry> result = courierStatsService.getStoreEntries(courierId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStoreName()).isEqualTo("Ataşehir MMM Migros");
        assertThat(result.get(1).getStoreName()).isEqualTo("Novada MMM Migros");

        verify(courierRepository).findById(courierId);
        verify(storeEntryRepository).findAllByCourierId(courierId);
    }

    @Test
    @DisplayName("Should return empty list when courier has no store entries")
    void shouldReturnEmptyListWhenNoStoreEntries() {
        Long courierId = 1L;
        Courier courier = Courier.builder()
                .id(courierId)
                .name("Test Courier")
                .totalDistance(1000.0)
                .build();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(storeEntryRepository.findAllByCourierId(courierId)).thenReturn(List.of());

        List<StoreEntry> result = courierStatsService.getStoreEntries(courierId);

        assertThat(result).isEmpty();
        verify(courierRepository).findById(courierId);
        verify(storeEntryRepository).findAllByCourierId(courierId);
    }

    @Test
    @DisplayName("Should throw exception when courier not found for store entries query")
    void shouldThrowExceptionWhenCourierNotFoundForStoreEntries() {
        Long courierId = 999L;
        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierStatsService.getStoreEntries(courierId))
                .isInstanceOf(CourierNotFoundException.class)
                .hasMessageContaining("999");

        verify(courierRepository).findById(courierId);
        verify(storeEntryRepository, never()).findAllByCourierId(any());
    }

    @Test
    @DisplayName("Should return zero distance for new courier")
    void shouldReturnZeroDistanceForNewCourier() {
        Long courierId = 1L;
        Courier newCourier = Courier.builder()
                .id(courierId)
                .name("New Courier")
                .totalDistance(0.0)
                .build();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(newCourier));

        double distance = courierStatsService.getTotalTravelDistance(courierId);

        assertThat(distance).isZero();
    }
}

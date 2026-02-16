package com.migros.courier_tracking.domain.service;

import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.model.Store;
import com.migros.courier_tracking.domain.service.strategy.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Proximity Checker Tests")
@ExtendWith(MockitoExtension.class)
class ProximityCheckerTest {

    @Mock
    private DistanceCalculator distanceCalculator;

    private ProximityChecker proximityChecker;

    @BeforeEach
    void setUp() {
        proximityChecker = new ProximityChecker(distanceCalculator);
    }

    @Test
    @DisplayName("Should return true when courier is within 100 meters of store")
    void shouldReturnTrueWhenWithinRadius() {
        Location courierLocation = createLocation(40.9923307, 29.1244229);
        Store store = createStore("Test Store", 40.9923307, 29.1244229);

        when(distanceCalculator.calculate(any(), any())).thenReturn(50.0);

        boolean isWithin = proximityChecker.isWithinStoreRadius(courierLocation, store);

        assertThat(isWithin)
                .as("Courier should be within 100m radius")
                .isTrue();

        verify(distanceCalculator).calculate(any(), any());
    }

    @Test
    @DisplayName("Should return false when courier is beyond 100 meters of store")
    void shouldReturnFalseWhenBeyondRadius() {
        Location courierLocation = createLocation(40.9923307, 29.1244229);
        Store store = createStore("Test Store", 40.9923307, 29.1244229);

        when(distanceCalculator.calculate(any(), any())).thenReturn(150.0);

        boolean isWithin = proximityChecker.isWithinStoreRadius(courierLocation, store);

        assertThat(isWithin)
                .as("Courier should be beyond 100m radius")
                .isFalse();

        verify(distanceCalculator).calculate(any(), any());
    }

    @Test
    @DisplayName("Should return true when courier is exactly at 100 meters boundary")
    void shouldReturnTrueAtExactBoundary() {
        Location courierLocation = createLocation(40.9923307, 29.1244229);
        Store store = createStore("Test Store", 40.9923307, 29.1244229);

        when(distanceCalculator.calculate(any(), any())).thenReturn(100.0);

        boolean isWithin = proximityChecker.isWithinStoreRadius(courierLocation, store);

        assertThat(isWithin)
                .as("Courier at exactly 100m should be considered within radius")
                .isTrue();
    }

    @Test
    @DisplayName("Should return true when courier is at store location (zero distance)")
    void shouldReturnTrueWhenAtStoreLocation() {
        Location courierLocation = createLocation(40.9923307, 29.1244229);
        Store store = createStore("Test Store", 40.9923307, 29.1244229);

        when(distanceCalculator.calculate(any(), any())).thenReturn(0.0);

        boolean isWithin = proximityChecker.isWithinStoreRadius(courierLocation, store);

        assertThat(isWithin)
                .as("Courier at store location should be within radius")
                .isTrue();
    }

    @Test
    @DisplayName("Should return false when courier is just barely beyond 100 meters")
    void shouldReturnFalseWhenJustBeyondRadius() {
        Location courierLocation = createLocation(40.9923307, 29.1244229);
        Store store = createStore("Test Store", 40.9923307, 29.1244229);

        when(distanceCalculator.calculate(any(), any())).thenReturn(100.1);

        boolean isWithin = proximityChecker.isWithinStoreRadius(courierLocation, store);

        assertThat(isWithin)
                .as("Courier at 100.1m should be beyond radius")
                .isFalse();
    }

    @Test
    @DisplayName("Should use DistanceCalculator to compute distance")
    void shouldUseDistanceCalculatorToComputeDistance() {
        Location courierLocation = createLocation(40.9923307, 29.1244229);
        Store store = createStore("Test Store", 40.986106, 29.1161293);

        when(distanceCalculator.calculate(any(), any())).thenReturn(750.0);

        proximityChecker.isWithinStoreRadius(courierLocation, store);

        verify(distanceCalculator).calculate(any(Location.class), any(Location.class));
    }

    private Location createLocation(double lat, double lng) {
        return Location.builder()
                .courierId(1L)
                .latitude(lat)
                .longitude(lng)
                .timestamp(Instant.now())
                .build();
    }

    private Store createStore(String name, double lat, double lng) {
        return Store.builder()
                .id(1L)
                .name(name)
                .latitude(lat)
                .longitude(lng)
                .build();
    }
}

package com.migros.courier_tracking.domain.service.strategy;

import com.migros.courier_tracking.domain.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Haversine Distance Calculator Tests")
class HaversineDistanceCalculatorTest {

    private HaversineDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new HaversineDistanceCalculator();
    }

    @Test
    @DisplayName("Should calculate distance between two Istanbul locations accurately")
    void shouldCalculateDistanceBetweenTwoIstanbulLocations() {
        Location atasehir = createLocation(40.9923307, 29.1244229);
        Location novada = createLocation(40.986106, 29.1161293);

        double distance = calculator.calculate(atasehir, novada);

        assertThat(distance)
                .as("Distance between Ataşehir and Novada stores")
                .isBetween(950.0, 1000.0);
    }

    @Test
    @DisplayName("Should return near-zero distance for same location")
    void shouldReturnZeroForSameLocation() {
        Location location = createLocation(40.9923307, 29.1244229);

        double distance = calculator.calculate(location, location);

        assertThat(distance)
                .as("Distance from location to itself")
                .isLessThan(0.1);
    }

    @Test
    @DisplayName("Should detect location within 100 meters")
    void shouldDetectLocationWithin100Meters() {
        Location store = createLocation(40.9923307, 29.1244229);
        Location nearby = createLocation(40.9927807, 29.1244229);

        double distance = calculator.calculate(store, nearby);

        assertThat(distance)
                .as("Distance should be within store radius")
                .isLessThan(100.0);
    }

    @Test
    @DisplayName("Should calculate longer distance accurately (>10km)")
    void shouldCalculateLongerDistance() {
        Location atasehir = createLocation(40.9923307, 29.1244229);
        Location beylikduzu = createLocation(41.0066851, 28.6552262);

        double distance = calculator.calculate(atasehir, beylikduzu);


        assertThat(distance)
                .as("Long distance calculation")
                .isGreaterThan(30000.0)  // > 30 km
                .isLessThan(45000.0);    // < 45 km
    }

    @Test
    @DisplayName("Should be symmetric (A to B equals B to A)")
    void shouldBeSymmetric() {
        Location locationA = createLocation(40.9923307, 29.1244229);
        Location locationB = createLocation(40.986106, 29.1161293);

        double distanceAtoB = calculator.calculate(locationA, locationB);
        double distanceBtoA = calculator.calculate(locationB, locationA);

        assertThat(distanceAtoB)
                .as("Distance should be symmetric")
                .isEqualTo(distanceBtoA, within(0.1));
    }

    @Test
    @DisplayName("Should handle locations at different longitudes")
    void shouldHandleDifferentLongitudes() {
        Location locationA = createLocation(40.0, 29.0);
        Location locationB = createLocation(40.0, 30.0);

        double distance = calculator.calculate(locationA, locationB);

        assertThat(distance)
                .as("Should calculate east-west distance")
                .isGreaterThan(0.0);
    }

    @Test
    @DisplayName("Should handle locations at different latitudes")
    void shouldHandleDifferentLatitudes() {
        Location locationA = createLocation(40.0, 29.0);
        Location locationB = createLocation(41.0, 29.0);

        double distance = calculator.calculate(locationA, locationB);

        assertThat(distance)
                .as("Should calculate north-south distance")
                .isBetween(110000.0, 112000.0);
    }

    @Test
    @DisplayName("Should handle very small distances (precision test)")
    void shouldHandleVerySmallDistances() {
        Location locationA = createLocation(40.9923307, 29.1244229);
        Location locationB = createLocation(40.9923316, 29.1244229);

        double distance = calculator.calculate(locationA, locationB);

        assertThat(distance)
                .as("Should handle very small distances")
                .isLessThan(2.0)
                .isGreaterThan(0.0);
    }

    private Location createLocation(double lat, double lng) {
        return Location.builder()
                .courierId(1L)
                .latitude(lat)
                .longitude(lng)
                .timestamp(Instant.now())
                .build();
    }

    // Custom assertion helper
    private org.assertj.core.data.Offset<Double> within(double tolerance) {
        return org.assertj.core.data.Offset.offset(tolerance);
    }
}


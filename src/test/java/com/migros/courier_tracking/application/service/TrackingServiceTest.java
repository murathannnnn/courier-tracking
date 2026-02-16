package com.migros.courier_tracking.application.service;

import com.migros.courier_tracking.adapter.event.publisher.LocationEventPublisher;
import com.migros.courier_tracking.application.port.out.CourierRepository;
import com.migros.courier_tracking.application.port.out.LocationRepository;
import com.migros.courier_tracking.domain.exception.CourierNotFoundException;
import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.service.strategy.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DisplayName("Tracking Service Tests")
@ExtendWith(MockitoExtension.class)
class TrackingServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private DistanceCalculator distanceCalculator;

    @Mock
    private LocationEventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<Courier> courierCaptor;

    @Captor
    private ArgumentCaptor<Location> locationCaptor;

    private TrackingService trackingService;

    @BeforeEach
    void setUp() {
        trackingService = new TrackingService(
                locationRepository,
                courierRepository,
                distanceCalculator,
                eventPublisher
        );
    }

    @Test
    @DisplayName("Should save location when tracking new location")
    void shouldSaveLocation() {
        Long courierId = 1L;
        Location newLocation = createLocation(courierId, 40.9923307, 29.1244229);
        Courier courier = new Courier();
        courier.setId(courierId);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(locationRepository.save(any())).thenReturn(newLocation);
        when(locationRepository.findLastLocationByCourierId(courierId)).thenReturn(Optional.empty());

        trackingService.trackLocation(newLocation);

        verify(locationRepository).save(newLocation);
    }

    @Test
    @DisplayName("Should publish event after saving location")
    void shouldPublishEventAfterSaving() {
        Long courierId = 1L;
        Location newLocation = createLocation(courierId, 40.9923307, 29.1244229);
        Courier courier = new Courier();
        courier.setId(courierId);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(locationRepository.save(any())).thenReturn(newLocation);
        when(locationRepository.findLastLocationByCourierId(courierId)).thenReturn(Optional.empty());

        trackingService.trackLocation(newLocation);

        verify(eventPublisher).publishLocationUpdate(newLocation);
    }

    @Test
    @DisplayName("Should calculate distance when previous location exists")
    void shouldCalculateDistanceWhenPreviousLocationExists() {
        Location previousLocation = createLocationWithId(1L, 1L, 40.9923307, 29.1244229);
        Location newLocation = createLocation(1L, 40.986106, 29.1161293);
        Location savedLocation = createLocationWithId(2L, 1L, 40.986106, 29.1161293);

        Courier existingCourier = Courier.builder()
                .id(1L)
                .name("Courier-1")
                .totalDistance(1000.0)
                .build();

        when(locationRepository.save(newLocation)).thenReturn(savedLocation);
        when(locationRepository.findLastLocationByCourierId(1L)).thenReturn(Optional.of(previousLocation));
        when(distanceCalculator.calculate(previousLocation, savedLocation)).thenReturn(750.0);
        when(courierRepository.findById(1L)).thenReturn(Optional.of(existingCourier));
        when(courierRepository.save(any())).thenReturn(existingCourier);

        trackingService.trackLocation(newLocation);

        verify(distanceCalculator).calculate(previousLocation, savedLocation);
        verify(courierRepository).save(courierCaptor.capture());

        Courier savedCourier = courierCaptor.getValue();
        assertThat(savedCourier.getTotalDistance())
                .as("Total distance should be updated")
                .isEqualTo(1750.0); // 1000 + 750
    }

    @Test
    @DisplayName("Should not calculate distance when no previous location exists")
    void shouldNotCalculateDistanceWhenNoPreviousLocation() {
        Long courierId = 1L;
        Location newLocation = createLocation(courierId, 40.9923307, 29.1244229);
        Courier courier = new Courier();
        courier.setId(courierId);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(locationRepository.save(any())).thenReturn(newLocation);
        when(locationRepository.findLastLocationByCourierId(courierId)).thenReturn(Optional.empty());

        trackingService.trackLocation(newLocation);

        verify(distanceCalculator, never()).calculate(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when courier does not exist")
    void shouldThrowExceptionWhenCourierNotExists() {
        Location newLocation = createLocation(99L, 40.986106, 29.1161293);

        when(courierRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackingService.trackLocation(newLocation))
                .isInstanceOf(CourierNotFoundException.class)
                .hasMessage("Courier not found with id: 99");
    }

    @Test
    @DisplayName("Should not update distance when saved location is same as previous")
    void shouldNotUpdateDistanceWhenSameLocation() {
        Long courierId = 1L;
        Location previousLocation = createLocationWithId(1L, courierId, 40.9923307, 29.1244229);
        Location newLocation = createLocation(courierId, 40.9923307, 29.1244229);

        Courier courier = new Courier();
        courier.setId(courierId);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(locationRepository.save(newLocation)).thenReturn(previousLocation); // Returns same location
        when(locationRepository.findLastLocationByCourierId(courierId)).thenReturn(Optional.of(previousLocation));

        trackingService.trackLocation(newLocation);

        verify(distanceCalculator, never()).calculate(any(), any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle multiple location updates for same courier")
    void shouldHandleMultipleLocationUpdates() {
        Courier courier = Courier.builder()
                .id(1L)
                .name("Courier-1")
                .totalDistance(0.0)
                .build();

        Location loc1 = createLocationWithId(1L, 1L, 40.9923307, 29.1244229);
        Location loc2 = createLocationWithId(2L, 1L, 40.986106, 29.1161293);
        Location loc3 = createLocationWithId(3L, 1L, 41.0066851, 28.6552262);

        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        when(locationRepository.save(any())).thenReturn(loc1);
        when(locationRepository.findLastLocationByCourierId(1L)).thenReturn(Optional.empty());
        trackingService.trackLocation(loc1);

        when(locationRepository.save(any())).thenReturn(loc2);
        when(locationRepository.findLastLocationByCourierId(1L)).thenReturn(Optional.of(loc1));
        when(distanceCalculator.calculate(loc1, loc2)).thenReturn(750.0);
        trackingService.trackLocation(loc2);

        when(locationRepository.save(any())).thenReturn(loc3);
        when(locationRepository.findLastLocationByCourierId(1L)).thenReturn(Optional.of(loc2));
        when(distanceCalculator.calculate(loc2, loc3)).thenReturn(35000.0);
        trackingService.trackLocation(loc3);

        verify(eventPublisher, times(3)).publishLocationUpdate(any());

        verify(courierRepository, times(2)).save(courierCaptor.capture());
        Courier finalCourier = courierCaptor.getValue();
        assertThat(finalCourier.getTotalDistance()).isEqualTo(35750.0); // 750 + 35000
    }

    private Location createLocation(Long courierId, double lat, double lng) {
        return Location.builder()
                .courierId(courierId)
                .latitude(lat)
                .longitude(lng)
                .timestamp(Instant.now())
                .build();
    }

    private Location createLocationWithId(Long id, Long courierId, double lat, double lng) {
        return Location.builder()
                .id(id)
                .courierId(courierId)
                .latitude(lat)
                .longitude(lng)
                .timestamp(Instant.now())
                .build();
    }
}
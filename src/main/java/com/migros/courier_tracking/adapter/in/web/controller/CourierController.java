package com.migros.courier_tracking.adapter.in.web.controller;

import com.migros.courier_tracking.adapter.in.web.dto.common.ApiResponse;
import com.migros.courier_tracking.adapter.in.web.dto.request.CreateCourierRequest;
import com.migros.courier_tracking.adapter.in.web.dto.request.LocationUpdateRequest;
import com.migros.courier_tracking.adapter.in.web.dto.response.CreateCourierResponse;
import com.migros.courier_tracking.adapter.in.web.dto.response.StoreEntryResponse;
import com.migros.courier_tracking.adapter.in.web.dto.response.TotalDistanceResponse;
import com.migros.courier_tracking.adapter.in.web.mapper.LocationMapper;
import com.migros.courier_tracking.application.port.in.CreateCourierUseCase;
import com.migros.courier_tracking.application.port.in.GetCourierStatsUseCase;
import com.migros.courier_tracking.application.port.in.TrackLocationUseCase;
import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.Location;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
@Tag(name = "Courier Tracking", description = "APIs for tracking courier locations and querying statistics")
public class CourierController {

    private final TrackLocationUseCase trackLocationUseCase;
    private final GetCourierStatsUseCase getCourierStatsUseCase;
    private final CreateCourierUseCase createCourierUseCase;
    private final LocationMapper locationMapper;


    @PostMapping
    @Operation(
            summary = "Create a new courier",
            description = "Registers a new courier in the system. The returned ID must be used for subsequent location tracking."
    )
    public ResponseEntity<ApiResponse<CreateCourierResponse>> createCourier(@Valid @RequestBody CreateCourierRequest request) {
        Courier courier = createCourierUseCase.createCourier(request.name());

        CreateCourierResponse responseData = CreateCourierResponse.fromDomain(courier);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Courier created successfully", responseData));
    }

    @PostMapping("/locations")
    @Operation(summary = "Track courier location", description = "Submit a new location update for a courier")
    public ResponseEntity<ApiResponse<Void>> trackLocation(@Valid @RequestBody LocationUpdateRequest request) {
        log.info("Received location update for courier: {}", request.courierId());

        Location location = locationMapper.toDomain(request);
        trackLocationUseCase.trackLocation(location);

        ApiResponse<Void> response = ApiResponse.success("Location tracked successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{courierId}/total-distance")
    @Operation(summary = "Get total travel distance", description = "Retrieve the total distance traveled by a courier")
    public ResponseEntity<ApiResponse<TotalDistanceResponse>> getTotalDistance(@PathVariable Long courierId) {
        log.info("Getting total distance for courier: {}", courierId);

        double distance = getCourierStatsUseCase.getTotalTravelDistance(courierId);
        TotalDistanceResponse data = TotalDistanceResponse.of(courierId, distance);

        ApiResponse<TotalDistanceResponse> response = ApiResponse.success(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courierId}/store-entries")
    @Operation(summary = "Get store entries", description = "Retrieve all store entries for a courier")
    public ResponseEntity<ApiResponse<List<StoreEntryResponse>>> getStoreEntries(@PathVariable Long courierId) {
        log.info("Getting store entries for courier: {}", courierId);

        List<StoreEntryResponse> entries = locationMapper.toResponseList(
                getCourierStatsUseCase.getStoreEntries(courierId)
        );

        ApiResponse<List<StoreEntryResponse>> response = ApiResponse.success(entries);
        return ResponseEntity.ok(response);
    }
}

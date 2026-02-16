package com.migros.courier_tracking.adapter.out.persistence.repository;

import com.migros.courier_tracking.adapter.out.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLocationRepository extends JpaRepository<LocationEntity, Long> {

    @Query("SELECT l FROM LocationEntity l WHERE l.courierId = :courierId ORDER BY l.timestamp DESC LIMIT 1")
    Optional<LocationEntity> findLastLocationByCourierId(@Param("courierId") Long courierId);

    List<LocationEntity> findAllByCourierIdOrderByTimestampDesc(Long courierId);

    Boolean existsByCourierIdAndLatitudeAndLongitudeAndTimestamp(
            Long courierId, Double latitude, Double longitude, Instant timestamp);

}

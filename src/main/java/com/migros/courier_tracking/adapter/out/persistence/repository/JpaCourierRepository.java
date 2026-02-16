package com.migros.courier_tracking.adapter.out.persistence.repository;

import com.migros.courier_tracking.adapter.out.persistence.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCourierRepository extends JpaRepository<CourierEntity, Long> {
}
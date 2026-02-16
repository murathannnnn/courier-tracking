package com.migros.courier_tracking.adapter.out.persistence.repository;

import com.migros.courier_tracking.adapter.out.persistence.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStoreRepository extends JpaRepository<StoreEntity, Long> {
}

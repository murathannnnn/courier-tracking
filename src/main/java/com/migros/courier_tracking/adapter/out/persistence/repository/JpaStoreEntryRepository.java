package com.migros.courier_tracking.adapter.out.persistence.repository;

import com.migros.courier_tracking.adapter.out.persistence.entity.StoreEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStoreEntryRepository extends JpaRepository<StoreEntryEntity, Long> {

    List<StoreEntryEntity> findAllByCourierIdOrderByEntryTimeDesc(Long courierId);
}

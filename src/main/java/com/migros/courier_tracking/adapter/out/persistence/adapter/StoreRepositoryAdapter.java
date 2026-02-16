package com.migros.courier_tracking.adapter.out.persistence.adapter;

import com.migros.courier_tracking.adapter.out.persistence.mapper.StoreEntityMapper;
import com.migros.courier_tracking.adapter.out.persistence.repository.JpaStoreRepository;
import com.migros.courier_tracking.application.port.out.StoreRepository;
import com.migros.courier_tracking.domain.model.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreRepositoryAdapter implements StoreRepository {

    private final JpaStoreRepository jpaStoreRepository;
    private final StoreEntityMapper storeEntityMapper;

    @Override
    @Cacheable(value = "stores")
    public List<Store> findAll() {
        return jpaStoreRepository.findAll().stream()
                .map(storeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "stores", allEntries = true)
    public Store save(Store store) {
        var entity = storeEntityMapper.toEntity(store);
        var savedEntity = jpaStoreRepository.save(entity);
        return storeEntityMapper.toDomain(savedEntity);
    }
}

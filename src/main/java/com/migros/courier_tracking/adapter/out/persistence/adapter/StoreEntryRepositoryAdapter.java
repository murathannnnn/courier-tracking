package com.migros.courier_tracking.adapter.out.persistence.adapter;

import com.migros.courier_tracking.adapter.out.persistence.mapper.StoreEntryEntityMapper;
import com.migros.courier_tracking.adapter.out.persistence.repository.JpaStoreEntryRepository;
import com.migros.courier_tracking.application.port.out.StoreEntryRepository;
import com.migros.courier_tracking.domain.model.StoreEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreEntryRepositoryAdapter implements StoreEntryRepository {

    private final JpaStoreEntryRepository jpaStoreEntryRepository;
    private final StoreEntryEntityMapper storeEntryEntityMapper;

    @Override
    public StoreEntry save(StoreEntry storeEntry) {
        var entity = storeEntryEntityMapper.toEntity(storeEntry);
        var savedEntity = jpaStoreEntryRepository.save(entity);
        return storeEntryEntityMapper.toDomain(savedEntity);
    }

    @Override
    public List<StoreEntry> findAllByCourierId(Long courierId) {
        return jpaStoreEntryRepository.findAllByCourierIdOrderByEntryTimeDesc(courierId).stream()
                .map(storeEntryEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}

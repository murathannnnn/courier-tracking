package com.migros.courier_tracking.application.service;

import com.migros.courier_tracking.application.port.in.LogStoreEntryUseCase;
import com.migros.courier_tracking.application.port.out.StoreRepository;
import com.migros.courier_tracking.domain.model.Location;
import com.migros.courier_tracking.domain.service.StoreEntryProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreEntryService implements LogStoreEntryUseCase {
    private final StoreRepository storeRepository;
    private final StoreEntryProcessor storeEntryProcessor;
    @Override
    public void execute(Location location) {
        // Check each store independently with transaction isolation
        storeRepository.findAll().forEach(store ->
                storeEntryProcessor.processStoreEntry(location, store)
        );
    }
}

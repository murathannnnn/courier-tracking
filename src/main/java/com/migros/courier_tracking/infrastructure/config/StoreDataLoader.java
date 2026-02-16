package com.migros.courier_tracking.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.courier_tracking.application.port.out.StoreRepository;
import com.migros.courier_tracking.domain.model.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreDataLoader implements CommandLineRunner {

    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        loadStores();
    }

    private void loadStores() throws IOException {
        log.info("Loading stores from stores.json...");

        ClassPathResource resource = new ClassPathResource("stores.json");
        List<Map<String, Object>> storeData = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {}
        );

        storeData.forEach(data -> {
            Store store = Store.builder()
                    .name((String) data.get("name"))
                    .latitude(((Number) data.get("lat")).doubleValue())
                    .longitude(((Number) data.get("lng")).doubleValue())
                    .build();

            storeRepository.save(store);
            log.info("Loaded store: {}", store.getName());
        });

        log.info("Successfully loaded {} stores", storeData.size());
    }
}

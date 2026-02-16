package com.migros.courier_tracking.application.service;

import com.migros.courier_tracking.application.port.in.CreateCourierUseCase;
import com.migros.courier_tracking.application.port.out.CourierRepository;
import com.migros.courier_tracking.domain.model.Courier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierRegistrationService implements CreateCourierUseCase {

    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public Courier createCourier(String name) {
        log.info("Registering new courier with name: {}", name);

        Courier courier = Courier.builder()
                .name(name)
                .totalDistance(0.0)
                .build();

        return courierRepository.save(courier);
    }
}
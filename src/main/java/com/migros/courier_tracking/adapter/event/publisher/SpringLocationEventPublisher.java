package com.migros.courier_tracking.adapter.event.publisher;

import com.migros.courier_tracking.application.port.out.LocationEventPublisher;
import com.migros.courier_tracking.domain.event.LocationUpdatedEvent;
import com.migros.courier_tracking.domain.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringLocationEventPublisher implements LocationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishLocationUpdate(Location location) {
        log.debug("Publishing location update event for courier: {}", location.getCourierId());
        eventPublisher.publishEvent(new LocationUpdatedEvent(this, location));
    }


}
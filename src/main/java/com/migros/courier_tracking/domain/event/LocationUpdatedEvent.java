package com.migros.courier_tracking.domain.event;

import com.migros.courier_tracking.domain.model.Location;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LocationUpdatedEvent {
    private final Object source;
    private final Location location;


}
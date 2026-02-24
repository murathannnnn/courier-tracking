package com.migros.courier_tracking.application.port.out;

import com.migros.courier_tracking.domain.model.Location;

public interface LocationEventPublisher {
    void publishLocationUpdate(Location location);
}

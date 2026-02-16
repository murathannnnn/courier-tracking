package com.migros.courier_tracking.application.port.in;

import com.migros.courier_tracking.domain.model.Location;

public interface TrackLocationUseCase {

    void trackLocation(Location location);
}

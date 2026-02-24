package com.migros.courier_tracking.application.port.in;

import com.migros.courier_tracking.domain.model.Location;

public interface LogStoreEntryUseCase {
    void execute(Location location);
}

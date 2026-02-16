package com.migros.courier_tracking.domain.service.strategy;

import com.migros.courier_tracking.domain.model.Location;

public interface DistanceCalculator {

    double calculate(Location from, Location to);
}

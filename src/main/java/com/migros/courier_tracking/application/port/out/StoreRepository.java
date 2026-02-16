package com.migros.courier_tracking.application.port.out;

import com.migros.courier_tracking.domain.model.Store;

import java.util.List;

public interface StoreRepository {

    List<Store> findAll();

    Store save(Store store);
}

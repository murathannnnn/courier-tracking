package com.migros.courier_tracking.adapter.event.listener;
import com.migros.courier_tracking.application.port.in.LogStoreEntryUseCase;
import com.migros.courier_tracking.domain.event.LocationUpdatedEvent;
import com.migros.courier_tracking.domain.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class StoreEntryLogger {

    private final LogStoreEntryUseCase logStoreEntryUseCase;


    @EventListener
    public void onLocationUpdate(LocationUpdatedEvent event) {
        Location location = event.getLocation();
        log.debug("Processing location for courier {} - checking proximity to all stores",
                location.getCourierId());

        logStoreEntryUseCase.execute(location);
    }

}

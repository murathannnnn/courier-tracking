package com.migros.courier_tracking.adapter.out.redis;

import com.migros.courier_tracking.application.port.out.LastVisitRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheAdapter implements LastVisitRegistry {

    private static final String KEY_PREFIX = "courier:lastvisit:";
    private static final long TTL_SECONDS = 60; // 1 minute

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveLastVisit(Long courierId, String storeName, Instant timestamp) {
        String key = buildKey(courierId, storeName);
        String value = timestamp.toString();

        redisTemplate.opsForValue().set(key, value, TTL_SECONDS, TimeUnit.SECONDS);
        log.debug("Saved last visit for courier {} at store {}", courierId, storeName);
    }

    @Override
    public Optional<Instant> getLastVisit(Long courierId, String storeName) {
        String key = buildKey(courierId, storeName);
        String value = redisTemplate.opsForValue().get(key);

        if (value != null) {
            return Optional.of(Instant.parse(value));
        }
        return Optional.empty();
    }

    @Override
    public boolean canLogNewEntry(Long courierId, String storeName, Instant currentTime) {
        Optional<Instant> lastVisit = getLastVisit(courierId, storeName);

        if (lastVisit.isEmpty()) {
            return true; // No previous visit
        }

        // Check if more than 1 minute has passed
        Duration duration = Duration.between(lastVisit.get(), currentTime);
        boolean canLog = duration.getSeconds() >= TTL_SECONDS;

        log.debug("Can log new entry for courier {} at store {}: {} (seconds since last: {})",
                courierId, storeName, canLog, duration.getSeconds());

        return canLog;
    }

    private String buildKey(Long courierId, String storeName) {
        return KEY_PREFIX + courierId + ":" + storeName;
    }
}

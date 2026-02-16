package com.migros.courier_tracking.adapter.out.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.migros.courier_tracking.application.port.out.CourierCachePort;
import com.migros.courier_tracking.domain.model.Courier;
import com.migros.courier_tracking.domain.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
public class RedisCourierCacheAdapter implements CourierCachePort {

    private static final String COURIER_KEY_PREFIX = "courier:";
    private static final String LAST_LOCATION_KEY_PREFIX = "courier:lastlocation:";
    private static final Duration COURIER_TTL = Duration.ofMinutes(30);
    private static final Duration LOCATION_TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCourierCacheAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Optional<Courier> getCourier(Long courierId) {
        String key = buildCourierKey(courierId);
        String json = redisTemplate.opsForValue().get(key);

        if (json != null) {
            try {
                Courier courier = objectMapper.readValue(json, Courier.class);
                log.debug("Cache HIT: Courier {} retrieved from Redis", courierId);
                return Optional.of(courier);
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize courier from cache: {}", e.getMessage());
                redisTemplate.delete(key);
            }
        }

        log.debug("Cache MISS: Courier {} not found in Redis", courierId);
        return Optional.empty();
    }

    @Override
    public void saveCourier(Courier courier) {
        String key = buildCourierKey(courier.getId());

        try {
            String json = objectMapper.writeValueAsString(courier);
            redisTemplate.opsForValue().set(key, json, COURIER_TTL);
            log.debug("Cached courier {} in Redis with TTL={}", courier.getId(), COURIER_TTL);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize courier for caching: {}", e.getMessage());
        }
    }

    @Override
    public void evictCourier(Long courierId) {
        String key = buildCourierKey(courierId);
        Boolean deleted = redisTemplate.delete(key);

        if (Boolean.TRUE.equals(deleted)) {
            log.debug("Evicted courier {} from cache", courierId);
        } else {
            log.debug("Courier {} was not in cache (eviction no-op)", courierId);
        }
    }

    @Override
    public Optional<Location> getLastLocation(Long courierId) {
        String key = buildLastLocationKey(courierId);
        String json = redisTemplate.opsForValue().get(key);

        if (json != null) {
            try {
                Location location = objectMapper.readValue(json, Location.class);
                log.debug("Cache HIT: Last location for courier {} retrieved from Redis", courierId);
                return Optional.of(location);
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize location from cache: {}", e.getMessage());
                redisTemplate.delete(key);
            }
        }

        log.debug("Cache MISS: Last location for courier {} not found in Redis", courierId);
        return Optional.empty();
    }

    @Override
    public void saveLastLocation(Location location) {
        String key = buildLastLocationKey(location.getCourierId());

        try {
            String json = objectMapper.writeValueAsString(location);
            redisTemplate.opsForValue().set(key, json, LOCATION_TTL);
            log.debug("Cached last location for courier {} in Redis with TTL={}",
                    location.getCourierId(), LOCATION_TTL);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize location for caching: {}", e.getMessage());
        }
    }

    private String buildCourierKey(Long courierId) {
        return COURIER_KEY_PREFIX + courierId;
    }

    private String buildLastLocationKey(Long courierId) {
        return LAST_LOCATION_KEY_PREFIX + courierId;
    }
}

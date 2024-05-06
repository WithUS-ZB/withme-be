package com.withus.withmebe.common.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStringService {

  private final RedisTemplate<String, Object> redisTemplate;

  public void setValues(String key, Object value, Duration expiration) {
    redisTemplate.opsForValue().set(key, value, expiration);
  }

  public Object getValues(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public Boolean deleteKey(String key) {
    return redisTemplate.delete(key);
  }
}

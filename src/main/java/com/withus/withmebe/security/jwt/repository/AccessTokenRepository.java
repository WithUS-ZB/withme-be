package com.withus.withmebe.security.jwt.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccessTokenRepository {
  private final RedisTemplate<String, String> redisTemplate;
  private static final String ACCESS_TOKEN_PREFIX = "access-token: ";

  public void set(Long memberId, String accessToken, Duration expiration) {
    redisTemplate.opsForValue().set(getKeyByMemberId(memberId), accessToken, expiration);
  }

  public String get(Long memberId) {
    return redisTemplate.opsForValue().get(getKeyByMemberId(memberId));
  }

  public Boolean delete(Long memberId) {
    return redisTemplate.delete(getKeyByMemberId(memberId));
  }
  
  private String getKeyByMemberId(Long memberId){
    return ACCESS_TOKEN_PREFIX + memberId;
  }
}

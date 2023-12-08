package com.example.management_task.redis_session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCasheService {
    private final StringRedisTemplate redisTemplate;

    private static final String CACHE_PREFIX = "jwt:";
    private static final Duration TOKEN_EXPIRATION = Duration.ofDays(30);

    public void cacheToken(String jwt, String email) {
        String tokenKey = CACHE_PREFIX + email;
        redisTemplate.opsForValue().set(tokenKey, jwt, TOKEN_EXPIRATION);
        log.info("Token cached for email: {}", email);
    }

    public Optional<String> getCachedTokenForEmail(String email) {
        String tokenKey = CACHE_PREFIX + email;
        String jwt = redisTemplate.opsForValue().get(tokenKey);

        if (jwt != null) {
            log.info("Token found in cache for email: {}", email);
            return Optional.of(jwt);
        } else {
            log.info("Token not found in cache for email: {}", email);
            return Optional.empty();
        }
    }

    public void delete(String jwt) {
        String tokenKey = CACHE_PREFIX + jwt;
        redisTemplate.delete(tokenKey);
    }
}

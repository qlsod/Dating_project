package com.example.dating.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 키-벨류 설정
    public void setValues(String token, String email){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, email, Duration.ofMinutes(30));
    }

    public void setDeviceToken(String deviceToken, String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        if (redisTemplate.hasKey(email)) {
            throw new RuntimeException("해당 deviceToken이 이미 존재합니다.");
        }

        values.set(email, deviceToken, Duration.ofMinutes(30));
    }

    public void delDeviceToken(String email) {
        if (!redisTemplate.hasKey(email)) {
            throw new RuntimeException("해당 deviceToken이 존재하지 않습니다.");
        }

        redisTemplate.delete(email);
    }

    // 키값으로 벨류 가져오기
    public String getValues(String token){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    // 키-벨류 삭제
    public void delValues(String token) {
        if (!redisTemplate.hasKey(token)) {
            redisTemplate.delete(token.substring(7));
        } else {
            throw new RuntimeException("해당 토큰이 존재하지 않습니다.");
        }
    }
}

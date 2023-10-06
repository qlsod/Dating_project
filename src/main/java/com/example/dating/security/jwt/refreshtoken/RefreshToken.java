package com.example.dating.security.jwt.refreshtoken;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 24 * 14)
public class RefreshToken {
    @Id
    private String refreshToken;
    private String email;
}

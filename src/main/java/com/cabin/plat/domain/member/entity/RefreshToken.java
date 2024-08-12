package com.cabin.plat.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@Builder
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*14)
public class RefreshToken {

    @Id
    private Long id;

    private String refreshToken;

    @Indexed
    private String accessToken;
}

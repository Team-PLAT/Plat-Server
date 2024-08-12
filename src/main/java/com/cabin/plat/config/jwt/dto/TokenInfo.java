package com.cabin.plat.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenInfo {

    private String accessToken;
    private String refreshToken;
}

package com.cabin.plat.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SocialType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플"),
    NAVER("네이버");

    private final String name;
}

package com.cabin.plat.domain.member.entity;

public enum SocialType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플"),
    NAVER("네이버");

    private final String name;

    SocialType(String name) { this.name = name; }
}

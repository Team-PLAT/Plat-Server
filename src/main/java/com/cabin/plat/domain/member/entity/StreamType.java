package com.cabin.plat.domain.member.entity;

import lombok.Getter;

@Getter
public enum StreamType {
    APPLE_MUSIC("애플뮤직"),
    SPOTIFY("스포티파이");

    private final String name;

    StreamType(String name) { this.name = name; }
}

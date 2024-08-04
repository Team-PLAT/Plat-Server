package com.cabin.plat.domain.entity;

public enum StreamAccountType {
    APPLE_MUSIC,
    SPOTIFY;

    public static StreamAccountType fromString(String type) {
        try {
            return StreamAccountType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 StreamAccountType: " + type);
        }
    }
}
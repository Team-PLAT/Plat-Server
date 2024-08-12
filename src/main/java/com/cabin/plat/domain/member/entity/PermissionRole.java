package com.cabin.plat.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionRole {
    ADMIN("관리자"),
    CLIENT("클라이언트"),
    DEVELOPER("개발자");
    private final String toKorean;
}

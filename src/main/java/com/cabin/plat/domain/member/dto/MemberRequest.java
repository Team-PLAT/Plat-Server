package com.cabin.plat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberRequest {

    @Getter
    @Builder
    public static class Avatar {
        private String avatar;
    }
}

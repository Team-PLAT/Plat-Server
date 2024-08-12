package com.cabin.plat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @Builder
    public static class ProfileInfo {
        private Long memberId;
        private String nickname;
        private String avatar;
    }

    @Getter
    @Builder
    public static class MemberId {
        private Long memberId;
    }
}

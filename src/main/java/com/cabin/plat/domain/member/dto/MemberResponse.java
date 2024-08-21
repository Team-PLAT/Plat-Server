package com.cabin.plat.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @Builder
    public static class Avatar {
        private String avatar;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSignIn {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
        private Boolean isServiced;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberTokens {
        private String accessToken;
        private String refreshToken;
    }
}

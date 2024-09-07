package com.cabin.plat.domain.member.dto;

import com.cabin.plat.domain.member.entity.StreamType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponse {

    @Getter
    @Builder
    public static class ProfileInfo {
        @Schema(description = "회원의 고유 ID", example = "1")
        private Long memberId;

        @Schema(description = "회원의 닉네임", example = "plat")
        private String nickname;

        @Schema(description = "아바타 이미지 URL", example = "https://example.com/avatar.png")
        private String avatar;
    }

    @Getter
    @Builder
    public static class ProfileStreamType {
        @Schema(description = "회원의 스트리밍 계정 타입", example = "APPLE_MUSIC")
        private StreamType streamType;
    }

    @Getter
    @Builder
    public static class MemberId {
        @Schema(description = "회원의 고유 ID", example = "1")
        private Long memberId;
    }

    @Getter
    @Builder
    public static class Avatar {
        @Schema(description = "아바타 이미지 URL", example = "https://example.com/avatar.png")
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
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
        private String accessToken;

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
        private String refreshToken;
    }
}

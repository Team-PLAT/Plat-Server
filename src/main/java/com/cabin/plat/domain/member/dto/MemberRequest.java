package com.cabin.plat.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import com.cabin.plat.domain.member.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Avatar {
        @Schema(description = "아바타 이미지 URL", defaultValue = "https://example.com/avatar.png")
        private String avatar;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSignInByEncryptedUserIdentifier {
        @Schema(description = "암호화된 사용자 식별자", defaultValue = "encryptedUserId123")
        private String encryptedUserIdentifier;

        @Schema(description = "소셜 타입", defaultValue = "APPLE")
        private SocialType socialType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberAppleSocialSignIn {
        @Schema(description = "암호화된 사용자 식별자", defaultValue = "encryptedUserId123")
        private String encryptedUserIdentifier;

        @Schema(description = "사용자 전체 이름", defaultValue = "조플랫")
        private String fullName;

        @Schema(description = "사용자 이메일", defaultValue = "cho.plat@example.com")
        private String email;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberNickname {
        @Schema(description = "사용자 닉네임", defaultValue = "plat")
        private String nickname;
    }
}

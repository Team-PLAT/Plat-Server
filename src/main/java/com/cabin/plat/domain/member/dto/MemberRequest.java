package com.cabin.plat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

import com.cabin.plat.domain.member.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @Builder
    public static class Avatar {
        private String avatar;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSignInByEncryptedUserIdentifier {
        private String encryptedUserIdentifier;
        private SocialType socialType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberAppleSocialSignIn {
        private String encryptedUserIdentifier;
        private String fullName;
        private String email;
    }

    @Getter
    @Builder
    public static class MemberNickname {
        private String nickname;
    }
}

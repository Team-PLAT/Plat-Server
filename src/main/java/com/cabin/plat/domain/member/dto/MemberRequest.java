package com.cabin.plat.domain.member.dto;

import com.cabin.plat.domain.member.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSignIn {
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



}

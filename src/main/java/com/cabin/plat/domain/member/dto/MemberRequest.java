package com.cabin.plat.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {


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

package com.cabin.plat.domain.member.mapper;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.*;
import com.cabin.plat.config.jwt.dto.TokenInfo;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public MemberResponse.ProfileInfo toProfileInfo(Long memberId, String nickname, String avatar) {
        return MemberResponse.ProfileInfo.builder()
                .memberId(memberId)
                .nickname(nickname)
                .avatar(avatar)
                .build();
    }

    public MemberResponse.ProfileStreamType toProfileStreamType(StreamType streamType) {
        return MemberResponse.ProfileStreamType.builder()
                .streamType(streamType)
                .build();
    }

    public MemberResponse.MemberId toMemberId(Long memberId) {
        return MemberResponse.MemberId.builder()
                .memberId(memberId)
                .build();
    }
    public MemberResponse.MemberSignIn toMemberSignIn(final Member member, TokenInfo tokenInfo, Boolean isServiced) {
        return MemberResponse.MemberSignIn.builder()
                .memberId(member.getId())
                .isServiced(isServiced)
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }

    public Member toMember(final String clientId, SocialType socialType) {
        return Member.builder()
                .clientId(clientId)
                .nickname("")
                .name("")
                .socialType(socialType)
                .permissionRole(PermissionRole.ADMIN)
                .avatar("")
                .email("")
                .build();
    }
}

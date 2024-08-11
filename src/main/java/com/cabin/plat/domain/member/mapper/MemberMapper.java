package com.cabin.plat.domain.member.mapper;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;
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
}

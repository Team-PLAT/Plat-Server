package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.StreamType;

public interface MemberService {
    MemberResponse.ProfileInfo getProfileInfo(Long memberId);
    MemberResponse.MemberId updateStreamType(Long memberId, StreamType streamType);

}

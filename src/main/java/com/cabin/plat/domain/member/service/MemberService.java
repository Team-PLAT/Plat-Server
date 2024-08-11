package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;

public interface MemberService {
    MemberResponse.ProfileInfo getProfileInfo(Member member);

}

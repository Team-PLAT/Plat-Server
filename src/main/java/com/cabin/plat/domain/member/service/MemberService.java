package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.SocialType;

public interface MemberService {
    MemberResponse.MemberSignIn appleSocialSignIn(MemberRequest.MemberAppleSocialSignIn request, SocialType socialType);

    MemberResponse.MemberSignIn signIn(MemberRequest.MemberSignIn request);
}

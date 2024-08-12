package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.StreamType;
import org.springframework.web.multipart.MultipartFile;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.entity.SocialType;

public interface MemberService {
    MemberResponse.ProfileInfo getProfileInfo(Long memberId);

    MemberResponse.MemberId updateStreamType(Long memberId, StreamType streamType);

    MemberResponse.Avatar uploadAvatarImage(MultipartFile file);

    MemberResponse.MemberId updateAvatarUrl(Long memberId, String avatar);

    MemberResponse.MemberSignIn appleSocialSignIn(MemberRequest.MemberAppleSocialSignIn request, SocialType socialType);

    MemberResponse.MemberSignIn signIn(MemberRequest.MemberSignIn request);
}

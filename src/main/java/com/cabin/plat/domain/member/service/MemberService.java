package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.StreamType;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    MemberResponse.ProfileInfo getProfileInfo(Long memberId);
    MemberResponse.MemberId updateStreamType(Long memberId, StreamType streamType);
    MemberResponse.Avatar uploadAvatarImage(MultipartFile file);
    MemberResponse.MemberId updateAvatarUrl(Long memberId, String avatar);
}

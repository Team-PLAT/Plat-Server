package com.cabin.plat.domain.member.controller;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.dto.MemberResponse.ProfileInfo;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "멤버 관련 API 입니다.")
public class MemberController {
    private final MemberService memberService;

    // TODO: Spring Security 설정 후 매개변수 @AuthMember 로 변경
    @Operation(summary = "유저 프로필 조회", description = "멤버 아이디, 닉네임, 프로필 이미지 url을 응답으로 반환합니다.")
    @GetMapping("/profile")
    public BaseResponse<MemberResponse.ProfileInfo> getProfileInfo(Long memberId) {
        return BaseResponse.onSuccess(memberService.getProfileInfo(memberId));
    }

    @Operation(summary = "유저 스트리밍 계정 선택", description = "멤버의 스트리밍 계정 정보를 변경합니다.")
    @PostMapping("/profile")
    public BaseResponse<MemberResponse.MemberId> updateStreamType(Long memberId, @RequestParam StreamType streamType) {
        return BaseResponse.onSuccess(memberService.updateStreamType(memberId, streamType));
    }
}

package com.cabin.plat.domain.member.controller;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.dto.MemberResponse.ProfileInfo;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "멤버 관련 API 입니다.")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "유저 프로필 조회", description = "멤버 아이디, 닉네임, 프로필 이미지 url을 응답으로 반환합니다.")
    @GetMapping("/profile")
    public BaseResponse<MemberResponse.ProfileInfo> getProfileInfo(Member member) { // TODO: 매개변수에 @AuthMember 어노테이션 추가
        return BaseResponse.onSuccess(memberService.getProfileInfo(member));
    }
}

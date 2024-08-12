package com.cabin.plat.domain.member.controller;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up/apple")
    public BaseResponse<MemberResponse.MemberSignIn> appleSocialSignIn(
            MemberRequest.MemberAppleSocialSignIn request,
            @RequestParam SocialType socialType
            ) {

        return BaseResponse.onSuccess(memberService.appleSocialSignIn(request, socialType));
    }


}

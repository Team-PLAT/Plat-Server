package com.cabin.plat.domain.member.controller;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class AuthenticationController {

    private final MemberService memberService;


    @PostMapping("/sign-in")
    public BaseResponse<MemberResponse.MemberSignIn> signIn(
            @RequestBody MemberRequest.MemberSignIn request) {
        return BaseResponse.onSuccess(memberService.signIn(request));
    }

}

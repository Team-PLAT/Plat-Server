package com.cabin.plat.domain.member.controller;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "(로그인) 멤버 API", description = "로그인 관련 멤버 API 입니다.")
public class AuthenticationController {

    private final MemberService memberService;

    @Operation(summary = "로그인 API", description = "로그인 API 입니다.")
    @PostMapping("/sign-in")
    public BaseResponse<MemberResponse.MemberSignIn> signIn(
            @RequestBody MemberRequest.MemberSignInByEncryptedUserIdentifier request) {
        return BaseResponse.onSuccess(memberService.signIn(request));
    }

}

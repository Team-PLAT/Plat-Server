package com.cabin.plat.domain.member.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "멤버 관련 API 입니다.")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up/apple")
    public BaseResponse<MemberResponse.MemberSignIn> appleSocialSignIn(
            MemberRequest.MemberAppleSocialSignIn request,
            @RequestParam SocialType socialType
            ) {

        return BaseResponse.onSuccess(memberService.appleSocialSignIn(request, socialType));
    }

    @Operation(summary = "유저 프로필 조회", description = "멤버 아이디, 닉네임, 프로필 이미지 url을 응답으로 반환합니다.")
    @GetMapping("/profile")
    public BaseResponse<MemberResponse.ProfileInfo> getProfileInfo(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.getProfileInfo(member));
    }

    @Operation(summary = "유저 스트리밍 계정 선택", description = "멤버의 스트리밍 계정 정보를 변경합니다.")
    @PostMapping("/profile")
    public BaseResponse<MemberResponse.MemberId> updateStreamType(@AuthMember Member member, @RequestParam StreamType streamType) {
        return BaseResponse.onSuccess(memberService.updateStreamType(member, streamType));
    }

    // TODO: s3 저장 미구현
    @Operation(summary = "유저 프로필 사진 업로드", description = "아바타 이미지를 업로드하고 URL을 반환합니다.")
    @PostMapping("/profile/avatar/upload")
    public BaseResponse<MemberResponse.Avatar> uploadAvatarImage(@RequestParam("avatar") MultipartFile file) {
        return BaseResponse.onSuccess(memberService.uploadAvatarImage(file));
    }

    @Operation(summary = "유저 프로필 사진 변경", description = "아바타 이미지 URL을 받아 프로필을 업데이트합니다.")
    @PatchMapping("/profile/avatar")
    public BaseResponse<MemberResponse.MemberId> updateAvatarUrl(@AuthMember Member member,
                                                  @RequestBody MemberRequest.Avatar avatar) {
        return BaseResponse.onSuccess(memberService.updateAvatarUrl(member, avatar.getAvatar()));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 합니다. 멤버의 정보가 소프트 딜리트 됩니다.")
    @DeleteMapping("/resign")
    public BaseResponse<MemberResponse.MemberId> resign(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.resign(member));
    }
}

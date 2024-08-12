package com.cabin.plat.domain.member.controller;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberRequest.AvatarRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.dto.MemberResponse.MemberId;
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

    // TODO: s3 저장 미구현
    @Operation(summary = "유저 프로필 사진 업로드", description = "아바타 이미지를 업로드하고 URL을 반환합니다.")
    @PostMapping("/profile/avatar/upload")
    public BaseResponse<MemberResponse.Avatar> uploadAvatarImage(@RequestParam("avatar") MultipartFile file) {
        return BaseResponse.onSuccess(memberService.uploadAvatarImage(file));
    }

    @Operation(summary = "유저 프로필 사진 변경", description = "아바타 이미지 URL을 받아 프로필을 업데이트합니다.")
    @PatchMapping("/profile/avatar")
    public BaseResponse<MemberId> updateAvatarUrl(@RequestParam Long memberId,
                                                  @RequestBody MemberRequest.Avatar avatar) {
        return BaseResponse.onSuccess(memberService.updateAvatarUrl(memberId, avatar.getAvatar()));
    }
}

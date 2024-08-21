package com.cabin.plat.domain.member.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "회원 관련 API입니다. 사용자 프로필 조회, 업데이트 및 삭제, 이미지 업로드 기능을 제공합니다.")
public class MemberController {
    private final MemberService memberService;

//    @PostMapping("/sign-up/apple")
//    public BaseResponse<MemberResponse.MemberSignIn> appleSocialSignIn(
//            MemberRequest.MemberAppleSocialSignIn request,
//            @RequestParam SocialType socialType
//            ) {
//
//        return BaseResponse.onSuccess(memberService.appleSocialSignIn(request, socialType));
//    }

    @Operation(summary = "유저 프로필 조회",
            description = "회원의 아이디, 닉네임, 프로필 이미지 URL을 조회합니다. 인증된 회원의 프로필 정보를 반환합니다.")
    @GetMapping("/profile")
    public BaseResponse<MemberResponse.ProfileInfo> getProfileInfo(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.getProfileInfo(member));
    }

    @Operation(summary = "유저 스트리밍 계정 조회",
            description = "회원의 스트리밍 계정 정보를 조회합니다.")
    @GetMapping("/profile/stream-type")
    public BaseResponse<MemberResponse.ProfileStreamType> getProfileStreamType(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.getProfileStreamType(member));
    }

    @Operation(summary = "유저 스트리밍 계정 선택 (변경)",
            description = "회원의 스트리밍 계정 정보를 업데이트합니다. `streamType` 파라미터를 통해 새로운 스트리밍 계정 유형을 전달합니다.")
    @PatchMapping("/profile/stream-type")
    public BaseResponse<MemberResponse.MemberId> updateStreamType(@AuthMember Member member, @RequestParam StreamType streamType) {
        return BaseResponse.onSuccess(memberService.updateStreamType(member, streamType));
    }

    @Operation(summary = "유저 프로필 사진 업로드", description = "프로필 사진을 업로드하고 URL을 반환합니다. 이미지를 `image` 파라미터로 전송하여 프로필 사진을 변경하세요. \"유저 프로필 사진 변경\" API를 호출하기 직전에 사용해서 이미지의 URL을 받으세요.")
    @PostMapping(value = "/profile/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<MemberResponse.Avatar> uploadAvatarImage(@RequestPart(value = "image") MultipartFile image) {
        return BaseResponse.onSuccess(memberService.uploadAvatarImage(image));
    }

    @Operation(summary = "유저 프로필 사진 변경", description = "업로드한 이미지의 URL로 프로필 사진을 변경합니다. \"유저 프로필 사진 업로드\" API 에서 받은 이미지 URL을 `avatar` 객체에 담아 요청 본문으로 전달하세요.")
    @PatchMapping("/profile/avatar")
    public BaseResponse<MemberResponse.MemberId> updateAvatarUrl(@AuthMember Member member,
                                                  @RequestBody MemberRequest.Avatar avatar) {
        return BaseResponse.onSuccess(memberService.updateAvatarUrl(member, avatar.getAvatar()));
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴 처리합니다. 해당 회원의 정보가 소프트 딜리트 됩니다.")
    @DeleteMapping("/resign")
    public BaseResponse<MemberResponse.MemberId> resign(@AuthMember Member member) {
        return BaseResponse.onSuccess(memberService.resign(member));
    }

    @Operation(summary = "유저 프로필 닉네임 변경", description = "회원의 닉네임을 변경합니다. 새로운 닉네임을 요청 본문에 `nickname` 값에 넣어서 전송하세요.")
    @PatchMapping("/profile/nickname")
    public BaseResponse<MemberResponse.MemberId> updateNickname(@AuthMember Member member, @RequestBody MemberRequest.MemberNickname nickname) {
        return BaseResponse.onSuccess(memberService.updateNickname(member, nickname.getNickname()));
    }

    // TODO: name, email 정보 POST 하는 API
}

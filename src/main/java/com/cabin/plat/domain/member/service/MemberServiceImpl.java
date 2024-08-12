package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.dto.MemberResponse.Avatar;
import com.cabin.plat.domain.member.dto.MemberResponse.MemberId;
import com.cabin.plat.domain.member.dto.MemberResponse.ProfileInfo;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.mapper.MemberMapper;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public ProfileInfo getProfileInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return memberMapper.toProfileInfo(member.getId(), member.getNickname(), member.getAvatar());
    }

    @Override
    @Transactional
    public MemberId updateStreamType(Long memberId, StreamType streamType) {
        Member updateMember = findMemberById(memberId);
        updateMember.setStreamType(streamType);
        memberRepository.save(updateMember);
        return memberMapper.toMemberId(updateMember.getId());
    }

    @Override
    public Avatar uploadAvatarImage(MultipartFile file) {
        // TODO: S3 에 이미지 업로드
//        return new MemberResponse.Avatar(s3ImageComponent.uploadImage(image));
        return null;
    }

    @Override
    public MemberId updateAvatarUrl(Long memberId, String avatar) {
        Member updateMember = findMemberById(memberId);
        updateMember.setAvatar(avatar);
        memberRepository.save(updateMember);
        return memberMapper.toMemberId(updateMember.getId());
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}

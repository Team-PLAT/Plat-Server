package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.dto.MemberResponse.ProfileInfo;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.mapper.MemberMapper;
import com.cabin.plat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public ProfileInfo getProfileInfo(Member member) {
        return memberMapper.toProfileInfo(member.getId(), member.getNickname(), member.getAvatar());
    }
}

package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberResponse.MemberSignIn appleSocialSignIn(
            MemberRequest.MemberAppleSocialSignIn request,
            SocialType socialType) {
        Optional<Member> optionalMember = memberRepository.findByClientId(request.getEncryptedUserIdentifier());

        // 1. 해당 유저가 존재하지 않으면 : Member 객체 생성하고 DB에 저장
        if (optionalMember.isEmpty()) {
            // TODO: 새로운 유저를 만들고 디비에 저장 & JWT Token 생성
            return null;
        }
        // 2. 해당 유저가 존재한다면 : Member 객체를 DB에서 불러오고,
        return null;
    }
}

package com.cabin.plat.domain.service;

import static com.cabin.plat.domain.exception.ErrorMessage.GET_MEMBER_EXCEPTION;
import static com.cabin.plat.domain.exception.ErrorMessage.TOKEN_AUTH_ERROR;

import com.cabin.plat.domain.entity.Member;
import com.cabin.plat.domain.entity.StreamAccountType;
import com.cabin.plat.domain.exception.MemberException;
import com.cabin.plat.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(GET_MEMBER_EXCEPTION));
    }

    @Override
    public StreamAccountType getStreamAccountTypeById(Long id) {
        Member member = getMemberById(id);
        return member.getStreamAccountType();
    }

    @Override
    @Transactional
    public void signOut(Long id, String token) {
        Member member = getMemberById(id);

        validateToken(token);

        memberRepository.delete(member);
    }

    @Override
    @Transactional
    public Member updateAvatar(Long id, String avatar, String token) {
        Member member = getMemberById(id);

        validateToken(token);

        member.setAvatar(avatar);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateNickname(Long id, String newNickname, String token) {
        Member member = getMemberById(id);

        validateToken(token);

        member.setNickname(newNickname);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateStreamAccountType(Long id, StreamAccountType streamAccountType, String token) {
        Member member = getMemberById(id);

        validateToken(token);

        member.setStreamAccountType(streamAccountType);
        return memberRepository.save(member);
    }

    @Override
    public Long extractUserIdFromToken(String token) {
        // TODO: 토큰에서 유저 아이디 파싱
        return 1L;
    }

    private void validateToken(String token) throws MemberException {
        // TODO: 토큰 검증 로직
        if (false) {
            throw new MemberException(TOKEN_AUTH_ERROR);
        }
    }
}

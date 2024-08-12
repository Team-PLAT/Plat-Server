package com.cabin.plat.config.security.service;

import com.cabin.plat.config.security.CustomUserDetails;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String memberId) {
        return memberRepository.findById(Long.valueOf(memberId))
                .map(CustomUserDetails::new).orElseThrow(
                        () -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND)
                );
    }
}

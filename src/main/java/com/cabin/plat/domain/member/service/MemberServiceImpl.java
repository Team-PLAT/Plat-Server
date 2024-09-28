package com.cabin.plat.domain.member.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.dto.MemberResponse.*;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.mapper.MemberMapper;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.MemberErrorCode;
import com.cabin.plat.config.jwt.dto.TokenInfo;
import com.cabin.plat.config.jwt.service.JwtUtil;
import com.cabin.plat.domain.member.dto.MemberRequest;
import com.cabin.plat.domain.member.entity.PermissionRole;
import com.cabin.plat.domain.member.entity.RefreshToken;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.mapper.AuthenticationMapper;
import com.cabin.plat.domain.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationMapper authenticationMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberMapper memberMapper;

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

    @Override
    public MemberResponse.MemberSignIn signIn(MemberRequest.MemberSignInByEncryptedUserIdentifier request) {

        String clientId = request.getEncryptedUserIdentifier();
        SocialType socialType = request.getSocialType();

        Optional<Member> optionalMember = memberRepository.findByClientId(clientId);

        // 1. 해당 유저가 존재하지 않으면 : Member 객체 생성하고 DB에 저장 -> 회원가입
        if (optionalMember.isEmpty()) {
            // TODO: 새로운 유저를 만들고 디비에 저장 & JWT Token 생성
            return saveNewMember(clientId, socialType);
        }
        // 2. 해당 유저가 존재한다면 : Member 객체를 DB에서 불러오고, MemberSignIn response 반환
        // TODO: 토큰 유효 시간을 검사해서 accessToken 또는 refreshToken의 유효 기간이 만료되었을 때, 만료된 토큰을 각각 재발급해주는 로직 구현
        boolean isServiced = optionalMember.get().getName() != null;

        Member member = optionalMember.get();
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(optionalMember.get().getId());
        String refreshToken = optionalRefreshToken.get().getRefreshToken();
        String accessToken = optionalRefreshToken.get().getAccessToken();

        if (jwtUtil.isExpired(accessToken)) {
            if (jwtUtil.isExpired(refreshToken)) {
                return generateNewToken(member, isServiced);
            }
            String newAccessToken = jwtUtil.createAccessToken(member.getId(), member.getClientId(), member.getPermissionRole());
            TokenInfo tokenInfo = authenticationMapper.toTokenInfo(newAccessToken, refreshToken);


            refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken, newAccessToken));
            return memberMapper.toMemberSignIn(member, tokenInfo, isServiced);
        }

        TokenInfo tokenInfo = authenticationMapper.toTokenInfo(accessToken, refreshToken);
        return memberMapper.toMemberSignIn(member, tokenInfo, isServiced);

    }

    @Override
    public ProfileInfo getProfileInfo(Member member) {
        return memberMapper.toProfileInfo(member.getId(), member.getNickname(), member.getAvatar());
    }

    @Override
    public ProfileStreamType getProfileStreamType(Member member) {
        return memberMapper.toProfileStreamType(member.getStreamType());
    }

    @Override
    @Transactional
    public MemberId updateStreamType(Member member, StreamType streamType) {
        Member updateMember = findMemberById(member.getId());
        updateMember.setStreamType(streamType);
        memberRepository.save(updateMember);
        return memberMapper.toMemberId(updateMember.getId());
    }

    @Override
    @Transactional
    public MemberId updateAvatarUrl(Member member, String avatar) {
        Member updateMember = findMemberById(member.getId());
        updateMember.setAvatar(avatar);
        memberRepository.save(updateMember);
        return memberMapper.toMemberId(updateMember.getId());
    }

    @Override
    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private MemberResponse.MemberSignIn saveNewMember(String clientId, SocialType socialType) {
        Member member = memberMapper.toMember(clientId, socialType);
        Member newMember = memberRepository.save(member);

        return generateNewToken(newMember, false);
    }

    private MemberResponse.MemberSignIn generateNewToken(Member member, Boolean isServiced) {
        Long memberId = member.getId();
        String clientId = member.getClientId();
        PermissionRole permissionRole = member.getPermissionRole();

        MemberResponse.MemberTokens memberTokens = jwtUtil.refreshTokens(memberId, clientId, permissionRole);

        TokenInfo tokenInfo = authenticationMapper.toTokenInfo(memberTokens.getAccessToken(), memberTokens.getRefreshToken());

        return memberMapper.toMemberSignIn(member, tokenInfo, isServiced);
    }

    @Override
    @Transactional
    public MemberResponse.MemberId resign(Member member) {
        Member deleteMember = findMemberById(member.getId());
        deleteMember.delete();
        memberRepository.save(deleteMember);
        return memberMapper.toMemberId(deleteMember.getId());
    }

    @Override
    @Transactional
    public MemberResponse.MemberId updateNickname(Member member, String nickname) {
        Member updateMember = findMemberById(member.getId());
        updateMember.setNickname(nickname);
        memberRepository.save(updateMember);
        return memberMapper.toMemberId(updateMember.getId());
    }
}

package com.cabin.plat.domain.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.PermissionRole;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.member.repository.RefreshTokenRepository;
import com.cabin.plat.domain.test.service.TestService;
import com.cabin.plat.global.exception.RestApiException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TestService testService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private List<Member> members;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        members = createTestMembers();
        testService.addMockData(members.get(0));
        testService.addMockData(members.get(1));
        testService.addMockData(members.get(2));
    }

    private List<Member> createTestMembers() {
        List<Member> members = List.of(
                Member.builder()
                        .permissionRole(PermissionRole.CLIENT)
                        .clientId("0")
                        .name("이름0")
                        .email("이메일0")
                        .nickname("닉네임0")
                        .avatar("https://testimage1.avatar0/")
                        .streamType(StreamType.APPLE_MUSIC)
                        .socialType(SocialType.APPLE)
                        .build(),
                Member.builder()
                        .permissionRole(PermissionRole.CLIENT)
                        .clientId("1")
                        .name("이름1")
                        .email("이메일1")
                        .nickname("닉네임1")
                        .avatar("https://testimage1.avatar1/")
                        .streamType(StreamType.APPLE_MUSIC)
                        .socialType(SocialType.APPLE)
                        .build(),
                Member.builder()
                        .permissionRole(PermissionRole.CLIENT)
                        .clientId("2")
                        .name("이름2")
                        .email("이메일2")
                        .nickname("닉네임2")
                        .avatar("https://testimage1.avatar2/")
                        .streamType(StreamType.APPLE_MUSIC)
                        .socialType(SocialType.APPLE)
                        .build()
        );

        return memberRepository.saveAll(members);
    }

    @Nested
    class ResignTest {

        @Test
        @Disabled
        void 회원_탈퇴시_프로필_정보_삭제() {
            // given
            Member member = members.get(0);

            // when
            memberService.resign(member);

            // then
            assertThatThrownBy(() -> memberService.findMemberById(member.getId()))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        @Disabled
        void 회원_탈퇴시_리프레시토큰_삭제() {
            // given
            Member member = members.get(0);

            // when
            assertThat(refreshTokenRepository.findById(member.getId())).isNotEmpty();
            memberService.resign(member);

            // then
            assertThat(refreshTokenRepository.findById(member.getId())).isEmpty();
        }
    }
}
package com.cabin.plat.domain.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.cabin.plat.domain.member.entity.*;
import com.cabin.plat.domain.test.service.TestService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    private List<Member> members;

    @BeforeEach
    void setUp() {
        members = createTestMembers();
        testService.addMockData(members.get(0));
        testService.addMockData(members.get(1));
        testService.addMockData(members.get(2));
    }

    private List<Member> createTestMembers() {
        return List.of(
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
    }

    @Nested
    class ResignTest {

        @Test
        void 회원_탈퇴시_프로필_삭제() {
            // given
            Member member = members.get(0);

            // when
            memberService.resign(member);

            // then
            assertThat(memberService.findMemberById(member.getId())).isNull();
        }

        @Test
        void 회원_탈퇴시_다른_API_호출_불가() {
            // given
            Member member = members.get(0);

            // when
            memberService.resign(member);

            // then
        }

        @Test
        void 회원_탈퇴한_멤버의_트랙_조회_안됨() {
            // given
            Member member = members.get(0);

            // when
            memberService.resign(member);

            // then
        }

        @Test
        void 회원_탈퇴한_멤버의_플레이리스트_조회_안됨() {
            // given
            Member member = members.get(0);

            // when
            memberService.resign(member);

            // then
        }
    }
}
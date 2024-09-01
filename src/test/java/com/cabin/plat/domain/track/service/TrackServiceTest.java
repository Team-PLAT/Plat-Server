package com.cabin.plat.domain.track.service;

import static org.junit.jupiter.api.Assertions.*;

import com.cabin.plat.domain.member.entity.*;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.track.entity.Location;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.repository.LocationRepository;
import com.cabin.plat.domain.track.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TrackServiceTest {

    @Autowired
    private TrackService trackService;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Location location;
    private Track track;


    @BeforeEach
    void setUp() {
        member = createTestMember();
        location = createTestLocation();
        track = createTestTrack(member, location);
    }

    private Member createTestMember() {
        Member member = new Member(null, PermissionRole.CLIENT, "1", "이름", "이메일", "닉네임", "https://testimage.avatar/", StreamType.APPLE_MUSIC, SocialType.APPLE);
        return memberRepository.save(member);
    }

    private Location createTestLocation() {
        Location location = new Location(null, "장소이름", "주소", 138.31212, 19.31233);
        return locationRepository.save(location);
    }

    private Track createTestTrack(Member member, Location location) {
        Track track = new Track(null, member, location, "isrc", "트랙 본문", "https://testimage.com");
        return trackRepository.save(track);
    }

    @Test
    void 레파지토리_연결_테스트() {
        // Then
        var retrievedTrack = trackRepository.findById(track.getId());
        assertTrue(retrievedTrack.isPresent());
        assertEquals(track.getId(), retrievedTrack.get().getId());
        assertEquals(track.getIsrc(), retrievedTrack.get().getIsrc());
        assertEquals(track.getContent(), retrievedTrack.get().getContent());
        assertEquals(track.getImageUrl(), retrievedTrack.get().getImageUrl());
        assertEquals(member.getId(), retrievedTrack.get().getMember().getId());
        assertEquals(location.getId(), retrievedTrack.get().getLocation().getId());
    }

    @Test
    void getTracksByLocation() {
        // 좌표 4개를 받아서 내부에 포함된 모든 트랙 반환
    }

    @Test
    void getTrackById() {
        // 트랙 아이디로 트랙 디테일 조회
    }

    @Test
    void likeTrack() {
        // 트랙 좋아요 표시
    }

    @Test
    void addTrack() {
        // 트랙 게시
    }

    @Test
    void getTrackFeeds() {
        // 트랙 피드 조회
    }

    @Test
    void reportTrack() {
        // 트랙 신고
    }
}
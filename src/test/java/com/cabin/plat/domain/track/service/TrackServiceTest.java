package com.cabin.plat.domain.track.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import com.cabin.plat.domain.member.entity.*;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.track.entity.Location;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.repository.LocationRepository;
import com.cabin.plat.domain.track.repository.TrackRepository;
import java.util.ArrayList;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        List<Member> setUpMembers = createTestMembers();

        List<Location> setUpLocations = createTestLocations();

        createTestTracks(setUpMembers, setUpLocations);
    }

    private List<Member> createTestMembers() {
        List<Member> members = new ArrayList<>();

        Member member1 = new Member(null, PermissionRole.CLIENT, "1", "이름1", "이메일1", "닉네임1", "https://testimage1.avatar/", StreamType.APPLE_MUSIC, SocialType.APPLE);
        Member member2 = new Member(null, PermissionRole.CLIENT, "2", "이름2", "이메일2", "닉네임2", "https://testimage2.avatar/", StreamType.SPOTIFY, SocialType.GOOGLE);
        Member member3 = new Member(null, PermissionRole.CLIENT, "3", "이름3", "이메일3", "닉네임3", "https://testimage3.avatar/", StreamType.APPLE_MUSIC, SocialType.FACEBOOK);

        members.add(memberRepository.save(member1));
        members.add(memberRepository.save(member2));
        members.add(memberRepository.save(member3));

        return members;
    }

    private List<Location> createTestLocations() {
        List<Location> locations = new ArrayList<>();

        Location domitory18 = new Location(null, "", "경상북도 포항시 남구 지곡동 287", 36.017062, 129.321993);
        Location burgerKing = new Location(null, "버거킹 포항공대점", "경상북도 포항시 남구 청암로 77", 36.015733, 129.322700);
        Location c5 = new Location(null, "Apple Developer Academy @ POSTECH(애플 디벨로퍼 아카데미)", "경상북도 포항시 남구 청암로 77", 36.014335, 129.325951);
        Location liversAcademy = new Location(null, "", "경상북도 포항시 남구 효자동 222-24", 36.009002, 129.332852);
        Location young1Dae = new Location(null, "영일대 해수욕장", "경상북도 포항시 북구 두호동 1015", 36.056074, 129.378190);
        Location hwangLidanGil = new Location(null, "황리단길", "경상북도 경주시 황남동 포석로 일대", 35.837555, 129.209712);

        locations.add(locationRepository.save(domitory18));
        locations.add(locationRepository.save(burgerKing));
        locations.add(locationRepository.save(c5));
        locations.add(locationRepository.save(liversAcademy));
        locations.add(locationRepository.save(young1Dae));
        locations.add(locationRepository.save(hwangLidanGil));

        return locations;
    }

    private void createTestTracks(List<Member> members, List<Location> locations) {
        Track track1 = new Track(null, members.get(0), locations.get(0), "isrc1", "기숙사에서 한곡", "https://testimage1.com");
        Track track2 = new Track(null, members.get(1), locations.get(1), "isrc2", "버거킹마을 공연 최애 노래", "https://testimage2.com");
        Track track3 = new Track(null, members.get(2), locations.get(2), "isrc3", "아카데미는 이 노래지", "https://testimage3.com");
        Track track4 = new Track(null, members.get(0), locations.get(3), "isrc4", "리버스아카데미 노래", "https://testimage4.com");
        Track track5 = new Track(null, members.get(1), locations.get(4), "isrc5", "영일대는 이노래지", "https://testimage5.com");
        Track track6 = new Track(null, members.get(1), locations.get(5), "isrc5", "황리단길과 어울리는", "https://testimage5.com");

        trackRepository.save(track1);
        trackRepository.save(track2);
        trackRepository.save(track3);
        trackRepository.save(track4);
        trackRepository.save(track5);
        trackRepository.save(track6);
    }

    @Test
    void 레파지토리_연결_테스트() {
        // given when
        Location location = locationRepository.save(
                new Location(null, "긱사", "경상북도 포항시 남구 지곡동 287", 36.017062, 129.321993)
        );

        // Then
        var retrievedLocation = locationRepository.findById(location.getId());
        assertThat(retrievedLocation.isPresent()).isTrue();
        assertThat(location.getPlaceName()).isEqualTo(retrievedLocation.get().getPlaceName());
        assertThat(location.getAddress()).isEqualTo(retrievedLocation.get().getAddress());
        assertThat(location.getLatitude()).isEqualTo(retrievedLocation.get().getLatitude());
        assertThat(location.getLongitude()).isEqualTo(retrievedLocation.get().getLongitude());
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
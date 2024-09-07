package com.cabin.plat.domain.track.service;

import static org.assertj.core.api.Assertions.*;

import com.cabin.plat.domain.member.entity.*;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import com.cabin.plat.domain.track.entity.Location;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.entity.TrackReport;
import com.cabin.plat.domain.track.repository.LocationRepository;
import com.cabin.plat.domain.track.repository.TrackReportRepository;
import com.cabin.plat.domain.track.repository.TrackRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Autowired
    private TrackReportRepository trackReportRepository;

    private List<Member> members;
    private List<Location> locations;
    private List<Track> tracks;

    @BeforeEach
    void setUp() {
        members = createTestMembers();

        locations = createTestLocations();

        tracks = createTestTracks(members, locations);
    }

    private List<Member> createTestMembers() {
        List<Member> members = new ArrayList<>();

        Member member1 = new Member(null, PermissionRole.CLIENT, "1", "이름1", "이메일1", "닉네임1", "https://testimage1.avatar/", StreamType.APPLE_MUSIC, SocialType.APPLE);
        Member member2 = new Member(null, PermissionRole.CLIENT, "2", "이름2", "이메일2", "닉네임2", "https://testimage2.avatar/", StreamType.SPOTIFY, SocialType.APPLE);
        Member member3 = new Member(null, PermissionRole.CLIENT, "3", "이름3", "이메일3", "닉네임3", "https://testimage3.avatar/", StreamType.APPLE_MUSIC, SocialType.APPLE);

        members.add(memberRepository.save(member1));
        members.add(memberRepository.save(member2));
        members.add(memberRepository.save(member3));

        return members;
    }

    private List<Location> createTestLocations() {
        List<Location> locations = new ArrayList<>();

        Location domitory18 = new Location(null, "Dormitory 16 (DICE)", "경상북도 포항시 남구 지곡동 287", 36.017062, 129.321993);
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

    private List<Track> createTestTracks(List<Member> members, List<Location> locations) {
        List<Track> tracks = new ArrayList<>();
        Track track1 = new Track(null, members.get(0), locations.get(0), "isrc1", "기숙사에서 한곡", "https://testimage1.com");
        Track track2 = new Track(null, members.get(1), locations.get(1), "isrc2", "버거킹마을 공연 최애 노래", "https://testimage2.com");
        Track track3 = new Track(null, members.get(2), locations.get(2), "isrc3", "아카데미는 이 노래지", "https://testimage3.com");
        Track track4 = new Track(null, members.get(0), locations.get(3), "isrc4", "리버스아카데미 노래", "https://testimage4.com");
        Track track5 = new Track(null, members.get(1), locations.get(4), "isrc5", "영일대는 이노래지", "https://testimage5.com");
        Track track6 = new Track(null, members.get(1), locations.get(5), "isrc5", "황리단길과 어울리는", "https://testimage5.com");

        tracks.add(trackRepository.save(track1));
        tracks.add(trackRepository.save(track2));
        tracks.add(trackRepository.save(track3));
        tracks.add(trackRepository.save(track4));
        tracks.add(trackRepository.save(track5));
        tracks.add(trackRepository.save(track6));
        return tracks;
    }

    @Test
    void 레파지토리_연결_테스트() {
        // given when
        Location location = locationRepository.save(
                new Location(null, "Dormitory 16 (DICE)", "경상북도 포항시 남구 지곡동 287", 36.017062, 129.321993)
        );

        // Then
        var retrievedLocation = locationRepository.findById(location.getId());
        assertThat(retrievedLocation.isPresent()).isTrue();
        assertThat(location.getBuildingName()).isEqualTo(retrievedLocation.get().getBuildingName());
        assertThat(location.getAddress()).isEqualTo(retrievedLocation.get().getAddress());
        assertThat(location.getLatitude()).isEqualTo(retrievedLocation.get().getLatitude());
        assertThat(location.getLongitude()).isEqualTo(retrievedLocation.get().getLongitude());
    }

    @Test
    void 좌표_4개를_받아서_내부에_포함된_모든_트랙_반환() {
        // given
        TrackResponse.TrackMap expectedTrackMap1 = TrackResponse.TrackMap.builder()
                .trackId(2L)
                .isrc("isrc2")
                .isLiked(false)
                .longitude(129.322700)
                .latitude(36.015733)
                .build();

        TrackResponse.TrackMap expectedTrackMap2 = TrackResponse.TrackMap.builder()
                .trackId(3L)
                .isrc("isrc3")
                .isLiked(false)
                .longitude(129.325951)
                .latitude(36.014335)
                .build();

        // when
        TrackResponse.TrackMapList trackMapList = trackService.getTracksByLocation(members.get(0), 36.016512, 129.321285, 36.012527, 129.328229);
        List<TrackResponse.TrackMap> trackMaps = trackMapList.getTracks();

        // then
        assertThat(trackMaps.size()).isEqualTo(2);

        assertThat(trackMaps).contains(expectedTrackMap1);
        assertThat(trackMaps).contains(expectedTrackMap2);
    }

    @Test
    void 트랙_아이디로_트랙_디테일_조회() {
        // given
        Long trackId = tracks.get(0).getId();
        TrackResponse.MemberInfo memberInfo = TrackResponse.MemberInfo.builder()
                .memberId(members.get(0).getId())
                .memberNickname(members.get(0).getNickname())
                .avatar(members.get(0).getAvatar())
                .build();

        TrackResponse.TrackDetail expectedTrackDetail = TrackResponse.TrackDetail.builder()
                .trackId(trackId)
                .isrc("isrc1")
                .latitude(36.017062)
                .longitude(129.321993)
                .buildingName("Dormitory 16 (DICE)")
                .address("경상북도 포항시 남구 지곡동 287")
                .imageUrl("https://testimage1.com")
                .content("기숙사에서 한곡")
                .likeCount(0)
                .isLiked(false)
                .member(memberInfo)
                .build();

        // when
        TrackResponse.TrackDetail trackDetail = trackService.getTrackById(members.get(0), trackId);

        // then
        assertThat(trackDetail).isNotNull();
        assertThat(trackDetail.getIsrc()).isEqualTo("isrc1");
        assertThat(trackDetail.getLatitude()).isEqualTo(36.017062);
        assertThat(trackDetail.getLongitude()).isEqualTo(129.321993);
        assertThat(trackDetail.getBuildingName()).isEqualTo("Dormitory 16 (DICE)");
        assertThat(trackDetail.getAddress()).isEqualTo("경상북도 포항시 남구 지곡동 287");
        assertThat(trackDetail.getImageUrl()).isEqualTo("https://testimage1.com");
        assertThat(trackDetail.getContent()).isEqualTo("기숙사에서 한곡");
        assertThat(trackDetail.getLikeCount()).isEqualTo(0);
        assertThat(trackDetail.getIsLiked()).isEqualTo(false);
        assertThat(trackDetail.getMember()).usingRecursiveComparison().isEqualTo(memberInfo);
    }

    @Nested
    class TrackLikeTests {

        private Long trackId;
        private Member member0;
        private Member member1;

        @BeforeEach
        void setUp() {
            trackId = tracks.get(0).getId();
            member0 = members.get(0);
            member1 = members.get(1);
        }

        @Test
        void 트랙_좋아요_표시() {
            // when
            trackService.likeTrack(member0, trackId, true); // 멤버0이 멤버0이 올린 트랙 좋아요 표시
            trackService.likeTrack(member1, trackId, true); // 멤버1이 멤버0이 올린 트랙 좋아요 표시
            TrackResponse.TrackDetail trackDetail0 = trackService.getTrackById(member0, trackId); // 멤버0이 조회
            TrackResponse.TrackDetail trackDetail1 = trackService.getTrackById(member1, trackId); // 멤버1이 조회

            // then
            assertThat(trackDetail0.getLikeCount()).isEqualTo(2);
            assertThat(trackDetail1.getLikeCount()).isEqualTo(2);
        }

        @Test
        void 트랙_좋아요_중복() {
            // when
            trackService.likeTrack(member1, trackId, true); // 멤버1이 멤버0이 올린 트랙 좋아요 표시
            trackService.likeTrack(member1, trackId, true); // 멤버1이 멤버0이 올린 트랙 좋아요 중복 표시
            TrackResponse.TrackDetail trackDetail0 = trackService.getTrackById(member1, trackId); // 멤버1이 조회

            // then
            assertThat(trackDetail0.getLikeCount()).isEqualTo(1);
        }

        @Test
        void 트랙_좋아요_취소() {
            // when
            trackService.likeTrack(member0, trackId, true); // 멤버0이 멤버0이 올린 트랙 좋아요 표시
            trackService.likeTrack(member1, trackId, true); // 멤버1이 멤버0이 올린 트랙 좋아요 표시
            trackService.likeTrack(member1, trackId, false); // 멤버1이 멤버0이 올린 트랙 좋아요 취소
            TrackResponse.TrackDetail trackDetail0 = trackService.getTrackById(member1, trackId); // 멤버1이 조회

            // then
            assertThat(trackDetail0.getLikeCount()).isEqualTo(1);
        }

        @Test
        void 트랙_좋아요_표시_사용자_본인의_좋아요_정보() {
            // when
            trackService.likeTrack(member1, trackId, true); // 멤버1이 멤버0이 올린 트랙 좋아요 표시
            TrackResponse.TrackDetail trackDetail0 = trackService.getTrackById(member0, trackId);
            TrackResponse.TrackDetail trackDetail1 = trackService.getTrackById(member1, trackId);

            // then
            assertThat(trackDetail0.getIsLiked()).isFalse();
            assertThat(trackDetail1.getIsLiked()).isTrue();
        }
    }

    // MARK: 해당 테스트는 외부 API (역지오코딩) 에 종속적입니다.
    @Test
    void 트랙_게시() {
        // given
        Member member = members.get(0);
        TrackRequest.TrackUpload trackUpload = TrackRequest.TrackUpload.builder()
                .isrc("isrc9")
                .imageUrl("https://testimage9.com")
                .content("테스트9")
                .latitude(36.014188)
                .longitude(129.325802)
                .build();

        // when
        Long trackId = trackService.addTrack(member, trackUpload).getTrackId();

        // then
        TrackResponse.TrackDetail trackDetail = trackService.getTrackById(member, trackId);
        assertThat(trackDetail.getIsrc()).isEqualTo("isrc9");
        assertThat(trackDetail.getImageUrl()).isEqualTo("https://testimage9.com");
        assertThat(trackDetail.getContent()).isEqualTo("테스트9");
        assertThat(trackDetail.getLatitude()).isEqualTo(36.014188);
        assertThat(trackDetail.getLongitude()).isEqualTo(129.325802);
        assertThat(trackDetail.getAddress()).isEqualTo("경상북도 포항시 남구 지곡동");
        assertThat(trackDetail.getBuildingName()).isEqualTo("포항공대제1융합관");
        assertThat(trackDetail.getLikeCount()).isZero();
        assertThat(trackDetail.getIsLiked()).isFalse();
        assertThat(trackDetail.getMember().getMemberId()).isEqualTo(member.getId());
    }

    @Test
    void 트렉_피드_조회_개수만_확인() {
        // given
        Member member = members.get(0);

        // when
        List<TrackDetail> trackDetails = trackService.getTrackFeeds(member, 0, 20).getTrackDetails();

        // then
        assertThat(trackDetails).hasSize(6);
    }

    @Test
    void 트랙_피드_페이지네이션() {
        // given
        Member member = members.get(0);

        // when
        List<TrackDetail> firstPageTracks = trackService.getTrackFeeds(member, 0, 4).getTrackDetails();
        List<TrackDetail> secondPageTracks = trackService.getTrackFeeds(member, 1, 4).getTrackDetails();
        List<TrackDetail> thirdPageTracks = trackService.getTrackFeeds(member, 2, 4).getTrackDetails();

        // then
        assertThat(firstPageTracks).hasSize(4);
        assertThat(secondPageTracks).hasSize(2);
        assertThat(thirdPageTracks).hasSize(0);
    }

    @Test
    void 트랙_신고() {
        // given
        Long trackId = tracks.get(0).getId();
        Member member1 = members.get(1);

        // when
        TrackResponse.ReportId reportIdResponse = trackService.reportTrack(member1, trackId);
        Long reportId = reportIdResponse.getReportId();

        // then
        Optional<TrackReport> savedTrackReport = trackReportRepository.findById(reportId);

        assertThat(savedTrackReport.isPresent()).isTrue();
        assertThat(savedTrackReport.get().getReportTrackId()).isEqualTo(trackId);
        assertThat(savedTrackReport.get().getReportMemberId()).isEqualTo(member1.getId());

        assertThat(reportIdResponse.getReportId()).isEqualTo(savedTrackReport.get().getId());
    }
}
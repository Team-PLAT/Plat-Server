package com.cabin.plat.domain.track.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.PermissionRole;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetailList;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackMap;
import com.cabin.plat.domain.track.entity.Location;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.entity.TrackReport;
import com.cabin.plat.domain.track.repository.LocationRepository;
import com.cabin.plat.domain.track.repository.TrackLikeRepository;
import com.cabin.plat.domain.track.repository.TrackReportRepository;
import com.cabin.plat.domain.track.repository.TrackRepository;
import com.cabin.plat.global.exception.RestApiException;
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
    private MemberService memberService;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrackReportRepository trackReportRepository;

    @Autowired
    private TrackLikeRepository trackLikeRepository;

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

        Member member1 = new Member(null, PermissionRole.CLIENT, "1", "이름1", "이메일1", "닉네임1",
                "https://testimage1.avatar/", StreamType.APPLE_MUSIC, SocialType.APPLE);
        Member member2 = new Member(null, PermissionRole.CLIENT, "2", "이름2", "이메일2", "닉네임2",
                "https://testimage2.avatar/", StreamType.SPOTIFY, SocialType.APPLE);
        Member member3 = new Member(null, PermissionRole.CLIENT, "3", "이름3", "이메일3", "닉네임3",
                "https://testimage3.avatar/", StreamType.APPLE_MUSIC, SocialType.APPLE);

        members.add(memberRepository.save(member1));
        members.add(memberRepository.save(member2));
        members.add(memberRepository.save(member3));

        return members;
    }

    private List<Location> createTestLocations() {
        List<Location> locations = new ArrayList<>();

        Location domitory18 = new Location(null, "Dormitory 16 (DICE)", "경상북도 포항시 남구 지곡동 287", 36.017062, 129.321993);
        Location burgerKing = new Location(null, "버거킹 포항공대점", "경상북도 포항시 남구 청암로 77", 36.015733, 129.322700);
        Location c5 = new Location(null, "Apple Developer Academy @ POSTECH(애플 디벨로퍼 아카데미)", "경상북도 포항시 남구 청암로 77",
                36.014335, 129.325951);
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
        Track track2 = new Track(null, members.get(0), locations.get(1), "isrc2", "버거킹마을 공연 최애 노래",
                "https://testimage2.com");
        Track track3 = new Track(null, members.get(1), locations.get(2), "isrc3", "아카데미는 이 노래지",
                "https://testimage3.com");
        Track track4 = new Track(null, members.get(1), locations.get(3), "isrc4", "리버스아카데미 노래",
                "https://testimage4.com");
        Track track5 = new Track(null, members.get(2), locations.get(4), "isrc5", "영일대는 이노래지",
                "https://testimage5.com");
        Track track6 = new Track(null, members.get(2), locations.get(5), "isrc6", "황리단길과 어울리는",
                "https://testimage5.com");

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

    @Nested
    class getTracksByLocationTests {

        @Test
        void 트랙맵_조회_성공() {
            // given
            TrackResponse.TrackMap expectedTrackMap1 = TrackResponse.TrackMap.builder()
                    .isrc("isrc2")
                    .isLiked(false)
                    .longitude(129.322700)
                    .latitude(36.015733)
                    .build();

            TrackResponse.TrackMap expectedTrackMap2 = TrackResponse.TrackMap.builder()
                    .isrc("isrc3")
                    .isLiked(false)
                    .longitude(129.325951)
                    .latitude(36.014335)
                    .build();

            // when
            TrackResponse.TrackMapList trackMapList = trackService.getTracksByLocation(members.get(0), 36.016512,
                    129.321285, 36.012527, 129.328229);
            List<TrackResponse.TrackMap> trackMaps = trackMapList.getTracks();

            // then
            assertThat(trackMaps.size()).isEqualTo(2);
            assertThat(trackMaps.get(0)).usingRecursiveComparison()
                    .ignoringFields("trackId").isEqualTo(expectedTrackMap1);
            assertThat(trackMaps.get(1)).usingRecursiveComparison()
                    .ignoringFields("trackId").isEqualTo(expectedTrackMap2);
        }

        @Test
        void 트랙맵_조회_삭제된_트랙_안가져옴() {
            // given
            Member member0 = members.get(0);
            trackService.deleteTrack(member0, tracks.get(0).getId());
            trackService.deleteTrack(member0, tracks.get(1).getId());

            // when
            TrackResponse.TrackMapList trackMapList = trackService.getTracksByLocation(
                    members.get(0),
                    36,
                    129,
                    37,
                    130
            );
            List<TrackResponse.TrackMap> trackMaps = trackMapList.getTracks();

            // then
            List<Long> getTrackLists = trackMaps.stream()
                    .map(TrackMap::getTrackId)
                    .toList();
            assertThat(getTrackLists).doesNotContain(tracks.get(0).getId(), tracks.get(1).getId());
        }
    }

    @Nested
    class getTrackByIdTests {

        @Test
        void 트랙_조회_성공() {
            // given
            Long trackId = tracks.get(0).getId();
            TrackResponse.MemberInfo memberInfo = TrackResponse.MemberInfo.builder()
                    .memberId(members.get(0).getId())
                    .memberNickname(members.get(0).getNickname())
                    .avatar(members.get(0).getAvatar())
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

        @Test
        void 회원탈퇴한_회원의_트랙정보는_노래만_남는다() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            memberService.resign(member0);

            // when
            TrackDetail trackDetail1 = trackService.getTrackById(member1, tracks.get(0).getId());
            TrackDetail trackDetail2 = trackService.getTrackById(member1, tracks.get(1).getId());

            // then
            for (TrackDetail trackDetail : List.of(trackDetail1, trackDetail2)) {
                assertThat(trackDetail.getTrackId()).isNotNull();
                assertThat(trackDetail.getIsrc()).isNotBlank();
                assertThat(trackDetail.getCreatedAt()).isNotNull();
                assertThat(trackDetail.getLatitude()).isZero();
                assertThat(trackDetail.getLongitude()).isZero();
                assertThat(trackDetail.getBuildingName()).isBlank();
                assertThat(trackDetail.getAddress()).isBlank();
                assertThat(trackDetail.getImageUrl()).isEqualTo("");
                assertThat(trackDetail.getContent()).isEqualTo("삭제된 게시글 입니다");
                assertThat(trackDetail.getLikeCount()).isZero();
                assertThat(trackDetail.getIsLiked()).isFalse();
                assertThat(trackDetail.getMember().getMemberNickname()).isEqualTo("알수없음");
                assertThat(trackDetail.getMember().getAvatar()).isEqualTo("");
            }
        }
    }

    @Nested
    class likeTrackTest {

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
    void addTrackTest() {
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
    void 겹치는_트랙이_있으면_위치를_조정한다() {
        // given
        Member member = members.get(0);
        TrackRequest.TrackUpload trackUpload1 = TrackRequest.TrackUpload.builder()
                .isrc("isrc9")
                .imageUrl("https://testimage9.com")
                .content("테스트9")
                .latitude(36.014188)
                .longitude(129.325802)
                .build();

        TrackRequest.TrackUpload trackUpload2 = TrackRequest.TrackUpload.builder()
                .isrc("test")
                .imageUrl("https://testtest.com")
                .content("테스트test")
                .latitude(36.014188)
                .longitude(129.325802)
                .build();

        // when
        Long trackId1 = trackService.addTrack(member, trackUpload1).getTrackId();
        Long trackId2 = trackService.addTrack(member, trackUpload2).getTrackId();

        // then
        TrackResponse.TrackDetail trackDetail2 = trackService.getTrackById(member, trackId2);
        assertThat(trackDetail2.getLatitude()).isNotEqualTo(36.014108);
        assertThat(trackDetail2.getLongitude()).isNotEqualTo(129.325841);
    }

    @Test
    void getTrackFeedsTest_개수만() {
        // given
        Member member = members.get(0);

        // when
        List<TrackDetail> trackDetails = trackService.getTrackFeeds(member, 0, 20).getTrackDetails();

        // then
        assertThat(trackDetails).hasSize(6);
    }

    @Nested
    class getTrackFeedsTests {

        @Test
        void getTrackFeedsTest_페이지네이션() {
            // given
            Member member = members.get(0);

            // when
            TrackDetailList firstPageTracks = trackService.getTrackFeeds(member, 0, 4);
            TrackDetailList secondPageTracks = trackService.getTrackFeeds(member, 1, 4);
            TrackDetailList thirdPageTracks = trackService.getTrackFeeds(member, 2, 4);

            // then
            assertThat(firstPageTracks.getTrackDetails()).hasSize(4);
            assertThat(firstPageTracks.isHasNext()).isTrue();
            assertThat(secondPageTracks.getTrackDetails()).hasSize(2);
            assertThat(secondPageTracks.isHasNext()).isFalse();
            assertThat(thirdPageTracks.getTrackDetails()).hasSize(0);
            assertThat(thirdPageTracks.isHasNext()).isFalse();
        }

        @Test
        void 회원탈퇴한_회원의_트랙은_트랙피드에_표시되지않는다() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            // when
            List<TrackDetail> member0Tracks1 = trackService.getTrackFeeds(member1, 0, 20).getTrackDetails();

            // then
            assertThat(member0Tracks1).hasSize(6);
            memberService.resign(member0);

            // when
            List<TrackDetail> member0Tracks = trackService.getTrackFeeds(member1, 0, 20).getTrackDetails();

            // then
            assertThat(member0Tracks).hasSize(4);
        }
    }

    @Nested
    class DeleteTrackTest {

        @Test
        void 트랙_삭제_성공() {
            // given
            Member uploader = members.get(0);
            Long trackId = tracks.get(0).getId();

            // when
            TrackResponse.TrackId deletedTrackId = trackService.deleteTrack(uploader, trackId);

            // then
            assertThat(deletedTrackId.getTrackId()).isEqualTo(trackId);
            Optional<Track> track = trackRepository.findById(trackId);
            assertThat(track.isPresent()).isTrue();
            assertThat(track.get().getDeletedAt()).isNotNull();
        }

        @Test
        void 트랙_삭제_실패_권한없음() {
            // given
            Member nonUploader = members.get(2); // 다른 멤버
            Long trackId = tracks.get(0).getId(); // 삭제하려는 트랙의 ID

            // when, then
            assertThatThrownBy(() -> trackService.deleteTrack(nonUploader, trackId))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 트랙_삭제시_노래_제외_정보들_삭제처리() {
            // given
            Member uploader = members.get(0);
            Long trackId = tracks.get(0).getId();

            // when
            TrackResponse.TrackId deletedTrackId = trackService.deleteTrack(uploader, trackId);

            // then
            trackRepository.findById(deletedTrackId.getTrackId()).ifPresent(track ->
                    assertThat(track.getDeletedAt()).isNotNull() // deletedAt 값이 null 이 아님
            );
            TrackDetail trackDetail = trackService.getTrackById(uploader, deletedTrackId.getTrackId());
            assertThat(trackDetail.getIsrc()).isEqualTo("isrc1");
            assertThat(trackDetail.getLikeCount()).isEqualTo(0); // 모든 좋아요 삭제
            assertThat(trackDetail.getIsLiked()).isFalse(); // 모든 좋아요 삭제
            assertThat(trackDetail.getContent()).isEqualTo("삭제된 게시글 입니다"); // 본문 "삭제된 게시글 입니다"
            assertThat(trackDetail.getImageUrl()).isEqualTo(""); // 트랙 이미지 ""
            assertThat(trackDetail.getMember().getMemberNickname()).isEqualTo("알수없음"); // 업로드한 멤버 이름 "알수없음"
            assertThat(trackDetail.getMember().getAvatar()).isEqualTo(""); // 업로드한 멤버 아바타 ""
        }
    }

    @Test
    void reportTrackTest() {
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
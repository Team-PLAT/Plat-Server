package com.cabin.plat.domain.playlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.PermissionRole;
import com.cabin.plat.domain.member.entity.SocialType;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.TrackOrder;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.Playlists.PlaylistInfo;
import com.cabin.plat.domain.playlist.entity.Playlist;
import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import com.cabin.plat.domain.playlist.repository.PlaylistRepository;
import com.cabin.plat.domain.playlist.repository.PlaylistTrackRepository;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import com.cabin.plat.domain.track.entity.Location;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.repository.LocationRepository;
import com.cabin.plat.domain.track.repository.TrackRepository;
import com.cabin.plat.domain.track.service.TrackService;
import com.cabin.plat.global.exception.RestApiException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlaylistServiceImplTest {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private PlaylistTrackRepository playlistTrackRepository;

    private List<Member> members;
    private List<Location> locations;
    private List<Track> tracks;
    private List<Long> playlistIds;

    @BeforeEach
    void setUp() {
        members = createTestMembers();
        locations = createTestLocations();
        tracks = createTestTracks(members, locations);
        List<PlaylistUpload> playlistUploads = createTestPlaylistUpload();
        playlistIds = createTestPlaylists(members, playlistUploads);
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
        Track track0 = new Track(null, members.get(0), locations.get(0), "isrc1", "기숙사에서 한곡", "https://testimage1.com");
        Track track1 = new Track(null, members.get(0), locations.get(1), "isrc2", "버거킹마을 공연 최애 노래",
                "https://testimage2.com");
        Track track2 = new Track(null, members.get(1), locations.get(2), "isrc3", "아카데미는 이 노래지",
                "https://testimage3.com");
        Track track3 = new Track(null, members.get(1), locations.get(3), "isrc4", "리버스아카데미 노래",
                "https://testimage4.com");
        Track track4 = new Track(null, members.get(2), locations.get(4), "isrc5", "영일대는 이노래지",
                "https://testimage5.com");
        Track track5 = new Track(null, members.get(2), locations.get(5), "isrc6", "황리단길과 어울리는",
                "https://testimage5.com");

        tracks.add(trackRepository.save(track0));
        tracks.add(trackRepository.save(track1));
        tracks.add(trackRepository.save(track2));
        tracks.add(trackRepository.save(track3));
        tracks.add(trackRepository.save(track4));
        tracks.add(trackRepository.save(track5));
        return tracks;
    }

    private List<PlaylistRequest.PlaylistUpload> createTestPlaylistUpload() {
        PlaylistRequest.PlaylistUpload playlistUpload0 = PlaylistUpload.builder()
                .title("플레이리스트 제목0")
                .playlistImageUrl("https://test0.com")
                .tracks(List.of(
                        TrackOrder.builder()
                                .trackId(tracks.get(0).getId())
                                .orderIndex(0)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(1).getId())
                                .orderIndex(1)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(2).getId())
                                .orderIndex(2)
                                .build()
                ))
                .build();

        PlaylistRequest.PlaylistUpload playlistUpload1 = PlaylistUpload.builder()
                .title("플레이리스트 제목1")
                .playlistImageUrl("https://test1.com")
                .tracks(List.of(
                        TrackOrder.builder()
                                .trackId(tracks.get(3).getId())
                                .orderIndex(0)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(4).getId())
                                .orderIndex(1)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(5).getId())
                                .orderIndex(2)
                                .build()
                ))
                .build();

        PlaylistRequest.PlaylistUpload playlistUpload2 = PlaylistUpload.builder()
                .title("플레이리스트 제목2")
                .playlistImageUrl("https://test2.com")
                .tracks(List.of(
                        TrackOrder.builder()
                                .trackId(tracks.get(0).getId())
                                .orderIndex(0)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(1).getId())
                                .orderIndex(1)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(2).getId())
                                .orderIndex(2)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(3).getId())
                                .orderIndex(3)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(4).getId())
                                .orderIndex(4)
                                .build()
                        , TrackOrder.builder()
                                .trackId(tracks.get(5).getId())
                                .orderIndex(5)
                                .build()
                ))
                .build();

        PlaylistRequest.PlaylistUpload playlistUpload3 = PlaylistUpload.builder()
                .title("플레이리스트 제목3")
                .playlistImageUrl("https://test3.com")
                .tracks(Collections.emptyList())
                .build();

        return List.of(playlistUpload0, playlistUpload1, playlistUpload2, playlistUpload3);
    }

    private List<Long> createTestPlaylists(List<Member> members, List<PlaylistUpload> playlistUploads) {
        Long playlistId0 = playlistService.addPlaylist(members.get(0), playlistUploads.get(0)).getPlaylistId();
        Long playlistId1 = playlistService.addPlaylist(members.get(1), playlistUploads.get(1)).getPlaylistId();
        Long playlistId2 = playlistService.addPlaylist(members.get(1), playlistUploads.get(2)).getPlaylistId();
        Long playlistId3 = playlistService.addPlaylist(members.get(1), playlistUploads.get(3)).getPlaylistId();
        return List.of(playlistId0, playlistId1, playlistId2, playlistId3);
    }

    @Nested
    class AddPlaylistTests {

        @Test
        void 플레이리스트_테이블_생성_테스트() {
            // given
            Long playlistId = playlistIds.get(0);

            // when
            List<Playlist> playlists = playlistRepository.findAll();
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId); // 저장된 플레이리스트 확인

            // then
//            assertThat(playlists).hasSize(4);
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");
            assertThat(playlist.getMember().getNickname()).isEqualTo("닉네임1");
        }

        @Test
        void 플레이리스트_트랙_관계_테이블_생성_테스트() {
            // given when
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAll();
            List<PlaylistTrack> filteredPlaylistTracks0 = playlistTracks.stream()
                    .filter(p -> p.getPlaylist().getTitle().equals("플레이리스트 제목0"))
                    .toList();
            List<PlaylistTrack> filteredPlaylistTracks1 = playlistTracks.stream()
                    .filter(p -> p.getPlaylist().getTitle().equals("플레이리스트 제목1"))
                    .toList();

            // then
            assertThat(filteredPlaylistTracks0).hasSize(3);
            assertThat(filteredPlaylistTracks0).allSatisfy(pt -> {
                assertThat(pt.getPlaylist().getTitle()).isEqualTo("플레이리스트 제목0");
                assertThat(pt.getPlaylist().getPlaylistImageUrl()).isEqualTo("https://test0.com");
                assertThat(pt.getTrack().getIsrc()).isIn("isrc1", "isrc2", "isrc3");
                assertThat(pt.getOrderIndex()).isIn(0, 1, 2);
            });

            assertThat(filteredPlaylistTracks1).hasSize(3);
            assertThat(filteredPlaylistTracks1).allSatisfy(pt -> {
                assertThat(pt.getPlaylist().getTitle()).isEqualTo("플레이리스트 제목1");
                assertThat(pt.getPlaylist().getPlaylistImageUrl()).isEqualTo("https://test1.com");
                assertThat(pt.getTrack().getIsrc()).isIn("isrc4", "isrc5", "isrc6");
                assertThat(pt.getOrderIndex()).isIn(0, 1, 2);
            });
        }

        @Test
        void 플레이리스트_생성_요청_OrderIndex_순서_잘못됨_예외발생() {
            // given
            PlaylistRequest.PlaylistUpload invalidPlaylistUpload = PlaylistUpload.builder()
                    .title("플레이리스트 제목0")
                    .playlistImageUrl("https://test0.com")
                    .tracks(List.of(
                            TrackOrder.builder()
                                    .trackId(tracks.get(0).getId())
                                    .orderIndex(0)
                                    .build()
                            , TrackOrder.builder()
                                    .trackId(tracks.get(1).getId())
                                    .orderIndex(1)
                                    .build()
                            , TrackOrder.builder()
                                    .trackId(tracks.get(2).getId())
                                    .orderIndex(1)
                                    .build()
                    ))
                    .build();

            // when then
            assertThatThrownBy(() -> playlistService.addPlaylist(members.get(0), invalidPlaylistUpload))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Nested
    class GetPlaylistsTests {
        // MARK: 본인이 업로드한 플레이리스트만 가져온다.

        @Test
        void 플레이리스트_목록_가져오기_성공() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            PlaylistInfo expectedPlaylistInfo1 = PlaylistInfo.builder()
                    .playlistId(playlistIds.get(0))
                    .title("플레이리스트 제목0")
                    .playlistImageUrl("https://test0.com")
                    .uploaderNicknames(Set.of(members.get(0).getNickname(), members.get(1).getNickname()))
                    .build();

            // when
            PlaylistResponse.Playlists playlists0 = playlistService.getPlaylists(member0, 0, 20);
            PlaylistResponse.Playlists playlists1 = playlistService.getPlaylists(member1, 0, 20);
            PlaylistResponse.Playlists.PlaylistInfo playlistInfo0 = playlists0.getPlaylists().get(0);

            // then
            assertThat(playlists0.getPlaylists()).hasSize(1);
            assertThat(playlists1.getPlaylists()).hasSize(3);
            assertThat(playlistInfo0)
                    .usingRecursiveComparison()
                    .ignoringFields("createdAt")
                    .isEqualTo(expectedPlaylistInfo1);
        }

        @Test
        void 플레이리스트_목록_가져오기_페이지네이션() {
            // given
            Member member1 = members.get(1);

            // when
            PlaylistResponse.Playlists pagedPlaylists0 = playlistService.getPlaylists(member1, 0, 1);
            PlaylistResponse.Playlists pagedPlaylists1 = playlistService.getPlaylists(member1, 1, 1);
            PlaylistResponse.Playlists pagedPlaylists2 = playlistService.getPlaylists(member1, 2, 1);
            PlaylistResponse.Playlists pagedPlaylists3 = playlistService.getPlaylists(member1, 3, 1);

            // then
            assertThat(pagedPlaylists0.getPlaylists()).hasSize(1);
            assertThat(pagedPlaylists0.isHasNext()).isTrue();
            assertThat(pagedPlaylists1.getPlaylists()).hasSize(1);
            assertThat(pagedPlaylists1.isHasNext()).isTrue();
            assertThat(pagedPlaylists2.getPlaylists()).hasSize(1);
            assertThat(pagedPlaylists2.isHasNext()).isFalse();
            assertThat(pagedPlaylists3.getPlaylists()).hasSize(0);
            assertThat(pagedPlaylists3.isHasNext()).isFalse();
        }

        @Test
        void 플레이리스트_목록_가져오기_없음() {
            // given
            Member member = members.get(2);

            // when
            PlaylistResponse.Playlists playlists = playlistService.getPlaylists(member, 0, 20);

            // then
            assertThat(playlists.getPlaylists()).isEmpty();
        }

        @Test
        void 회원_탈퇴한_멤버의_트랙이_포함된_플레이리스트_조회시_uploaderNickname_닉네임_알수없음으로_가져오기() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            memberService.resign(member1); // 멤버 1이 탈퇴 해서 멤버 1이 업로드한 트랙2가 삭제 (트랙2는 멤버0이 만든 플레이리스트0에 포함)

            // when
            PlaylistResponse.Playlists playlists0 = playlistService.getPlaylists(member0, 0, 20);

            // then
            String title = playlists0.getPlaylists().get(0).getTitle();
            String imageUrl = playlists0.getPlaylists().get(0).getPlaylistImageUrl();
            Set<String> uploaderNicknames = playlists0.getPlaylists().get(0).getUploaderNicknames();
            assertThat(title).isEqualTo("플레이리스트 제목0");
            assertThat(imageUrl).isEqualTo("https://test0.com");
            assertThat(uploaderNicknames).containsExactlyInAnyOrder(member0.getNickname(), "알수없음");
        }

        @Test
        void 삭제된_트랙이_포함된_플레이리스트_조회시_삭제된_트랙은_uploaderNickname_닉네임_알수없음으로_가져오기() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            trackService.deleteTrack(member1, tracks.get(2).getId()); // 멤버 1이 업로드한 트랙2 삭제 (트랙2는 멤버0이 만든 플레이리스트0에 포함)

            // when
            PlaylistResponse.Playlists playlists0 = playlistService.getPlaylists(member0, 0, 20);

            // then
            String title = playlists0.getPlaylists().get(0).getTitle();
            String imageUrl = playlists0.getPlaylists().get(0).getPlaylistImageUrl();
            Set<String> uploaderNicknames = playlists0.getPlaylists().get(0).getUploaderNicknames();
            assertThat(title).isEqualTo("플레이리스트 제목0");
            assertThat(imageUrl).isEqualTo("https://test0.com");
            assertThat(uploaderNicknames).containsExactlyInAnyOrder(member0.getNickname(), "알수없음");
        }

        @Test
        void 회원_탈퇴시_해당_회원이_올린_플레이리스트_모두_소프트_삭제() {
            // given
            Member member = members.get(0);
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("createdAt"));
            Pageable pageable = PageRequest.of(0, 20, Sort.by(sorts));

            // when
            memberService.resign(member);

            // then
            playlistRepository.findAllByMember(member, pageable).forEach(playlist -> {
                assertThat(playlist.getDeletedAt()).isNotNull();
            });
        }
    }

    @Nested
    class GetSearchedPlaylistsTests {

        @Test
        void 플레이리스트_제목_검색_2개_중에_하나만_검색_성공() {
            // given
            Member member = members.get(1);
            PlaylistResponse.Playlists.PlaylistInfo expectedPlaylistInfo = PlaylistResponse.Playlists.PlaylistInfo.builder()
                    .playlistId(playlistIds.get(1))
                    .title("플레이리스트 제목1")
                    .playlistImageUrl("https://test1.com")
                    .uploaderNicknames(Set.of(members.get(1).getNickname(), members.get(2).getNickname()))
                    .build();

            // when
            PlaylistResponse.Playlists playlists = playlistService.getSearchedPlaylists(member, "제목1", 0, 20);
            PlaylistResponse.Playlists.PlaylistInfo playlistInfo0 = playlists.getPlaylists().get(0);

            // then
            assertThat(playlists.getPlaylists()).hasSize(1);
            assertThat(playlistInfo0)
                    .usingRecursiveComparison()
                    .ignoringFields("createdAt")
                    .isEqualTo(expectedPlaylistInfo);
        }

        @Test
        void 플레이리스트_제목_검색_페이징() {
            // given
            Member member = members.get(1);

            // when
            PlaylistResponse.Playlists pagedPlaylists0 = playlistService.getSearchedPlaylists(member, "플레이리스트", 0, 1);
            PlaylistResponse.Playlists pagedPlaylists1 = playlistService.getSearchedPlaylists(member, "플레이리스트", 1, 1);
            PlaylistResponse.Playlists pagedPlaylists2 = playlistService.getSearchedPlaylists(member, "플레이리스트", 2, 1);
            PlaylistResponse.Playlists pagedPlaylists3 = playlistService.getSearchedPlaylists(member, "플레이리스트", 3, 1);

            // then
            assertThat(pagedPlaylists0.getPlaylists()).hasSize(1);
            assertThat(pagedPlaylists1.getPlaylists()).hasSize(1);
            assertThat(pagedPlaylists2.getPlaylists()).hasSize(1);
            assertThat(pagedPlaylists3.getPlaylists()).hasSize(0);
        }

        @Test
        void 플레이리스트_제목_검색_일치하는_플레이리스트_없음() {
            // given
            Member member = members.get(1);

            // when
            PlaylistResponse.Playlists playlists = playlistService.getSearchedPlaylists(member, "플레이리스트 제목 아님", 0, 20);

            // then
            assertThat(playlists.getPlaylists()).isEmpty();
        }

        @Test
        void 플레이리스트_제목_검색_남의_플레이리스트_검색_안됨() {
            // given
            Member member = members.get(0);

            // when
            PlaylistResponse.Playlists playlists = playlistService.getSearchedPlaylists(member, "플레이리스트 제목1", 0, 20);

            // then
            assertThat(playlists.getPlaylists()).isEmpty();
        }

        @Test
        void 회원_탈퇴한_멤버의_트랙이_포함된_플레이리스트_조회시_uploaderNickname_닉네임_알수없음으로_가져오기() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            memberService.resign(member1); // 멤버 1이 탈퇴 해서 멤버 1이 업로드한 트랙2가 삭제 (트랙2는 멤버0이 만든 플레이리스트0에 포함)

            // when
            PlaylistResponse.Playlists playlists0 = playlistService.getSearchedPlaylists(member0, "플레이리스트", 0, 20);

            // then
            String title = playlists0.getPlaylists().get(0).getTitle();
            String imageUrl = playlists0.getPlaylists().get(0).getPlaylistImageUrl();
            Set<String> uploaderNicknames = playlists0.getPlaylists().get(0).getUploaderNicknames();
            assertThat(title).isEqualTo("플레이리스트 제목0");
            assertThat(imageUrl).isEqualTo("https://test0.com");
            assertThat(uploaderNicknames).containsExactlyInAnyOrder(member0.getNickname(), "알수없음");
        }

        @Test
        void 삭제된_트랙이_포함된_플레이리스트_조회시_삭제된_트랙은_uploaderNickname_닉네임_알수없음으로_가져오기() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            trackService.deleteTrack(member1, tracks.get(2).getId()); // 멤버 1이 업로드한 트랙2 삭제 (트랙2는 멤버0이 만든 플레이리스트0에 포함)

            // when
            PlaylistResponse.Playlists playlists0 = playlistService.getSearchedPlaylists(member0, "플레이리스트", 0, 20);

            // then
            String title = playlists0.getPlaylists().get(0).getTitle();
            String imageUrl = playlists0.getPlaylists().get(0).getPlaylistImageUrl();
            Set<String> uploaderNicknames = playlists0.getPlaylists().get(0).getUploaderNicknames();
            assertThat(title).isEqualTo("플레이리스트 제목0");
            assertThat(imageUrl).isEqualTo("https://test0.com");
            assertThat(uploaderNicknames).containsExactlyInAnyOrder(member0.getNickname(), "알수없음");
        }
    }

    @Nested
    class DeletePlaylistTests {

        @Test
        void 플레이리스트_삭제_성공() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);

            // when
            PlaylistResponse.PlayListId deletedPlayListId = playlistService.deletePlaylist(member, playlistId);

            // then
            assertThat(deletedPlayListId.getPlaylistId()).isEqualTo(playlistId);
            Optional<Playlist> playlist = playlistRepository.findById(deletedPlayListId.getPlaylistId());
            assertThat(playlist.isPresent()).isTrue();
            assertThat(playlist.get().getDeletedAt()).isNotNull();

            PlaylistResponse.Playlists playlists = playlistService.getPlaylists(member, 0, 20);
            assertThat(playlists.getPlaylists()).hasSize(0);
        }

        @Test
        void 플레이리스트_삭제_실패_권한없음_예외발생() {
            // given
            Member member = members.get(1);
            Long playlistId = playlistIds.get(0);

            // when then
            assertThatThrownBy(() -> playlistService.deletePlaylist(member, playlistId))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Nested
    class GetPlaylistDetailTests {

        @Test
        void 플레이리스트_디테일_조회_성공_업로더() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);

            // when
            PlaylistResponse.PlaylistDetail playlistDetail = playlistService.getPlaylistDetail(member, playlistId);

            // then
            assertPlaylistTrackDetails(playlistDetail);
        }

        @Test
        void 플레이리스트_디테일_조회_성공_업로더_아님() {
            // given
            Member member = members.get(1);
            Long playlistId = playlistIds.get(0);

            // when
            PlaylistResponse.PlaylistDetail playlistDetail = playlistService.getPlaylistDetail(member, playlistId);

            // then
            assertPlaylistTrackDetails(playlistDetail);
        }

        @Test
        void 회원_탈퇴한_멤버의_트랙이_포함된_플레이리스트_디테일_조회시_노래정보_제외_나머지_삭제처리() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            Long playlist0Id = playlistIds.get(0);
            memberService.resign(member1);

            // when
            TrackDetail trackDetail = playlistService.getPlaylistDetail(member0, playlist0Id).getTracks().get(2)
                    .getTrackDetail(); // 탈퇴한 멤버가 업로드한 트랙

            // then
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

        @Test
        void 삭제한_트랙이_포함된_플레이리스트_디테일_조회시_노래정보_제외_나머지_삭제처리() {
            // given
            Member member0 = members.get(0);
            Member member1 = members.get(1);
            Long playlist0Id = playlistIds.get(0);

            // when
            trackService.deleteTrack(member1, tracks.get(2).getId());
            TrackDetail newTrackDetail = playlistService.getPlaylistDetail(member0, playlist0Id).getTracks().get(2)
                    .getTrackDetail(); // 탈퇴한 멤버가 업로드한 트랙

            // then
            assertThat(newTrackDetail.getIsrc()).isEqualTo("isrc3");
            assertThat(newTrackDetail.getLikeCount()).isEqualTo(0);
            assertThat(newTrackDetail.getIsLiked()).isFalse();
            assertThat(newTrackDetail.getContent()).isEqualTo("삭제된 게시글 입니다");
            assertThat(newTrackDetail.getImageUrl()).isEqualTo("");
            assertThat(newTrackDetail.getMember().getMemberNickname()).isEqualTo("알수없음");
            assertThat(newTrackDetail.getMember().getAvatar()).isEqualTo("");
        }

        private void assertPlaylistTrackDetails(PlaylistResponse.PlaylistDetail playlistDetail) {
            PlaylistResponse.TrackDetailOrder trackDetailOrder0 = playlistDetail.getTracks().get(0);
            PlaylistResponse.TrackDetailOrder trackDetailOrder1 = playlistDetail.getTracks().get(1);
            PlaylistResponse.TrackDetailOrder trackDetailOrder2 = playlistDetail.getTracks().get(2);

            assertThat(trackDetailOrder0.getOrderIndex()).isEqualTo(0);
            assertThat(trackDetailOrder1.getOrderIndex()).isEqualTo(1);
            assertThat(trackDetailOrder2.getOrderIndex()).isEqualTo(2);
            assertThat(trackDetailOrder0.getTrackDetail().getIsrc()).isEqualTo("isrc1");
            assertThat(trackDetailOrder1.getTrackDetail().getIsrc()).isEqualTo("isrc2");
            assertThat(trackDetailOrder2.getTrackDetail().getIsrc()).isEqualTo("isrc3");
        }
    }

    @Nested
    class UpdatePlaylistTests {

        private PlaylistRequest.PlaylistEdit playlistEdit = PlaylistRequest.PlaylistEdit.builder()
                .title("플레이리스트 제목2")
                .playlistImageUrl("https://test2.com")
                .build();

        @Test
        void 플레이리스트_수정_성공() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);

            // when
            playlistService.updatePlaylistTitleAndImage(member, playlistId, playlistEdit);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 변경된 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목2");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test2.com");
        }

        @Test
        void 플레이리스트_수정_실패_권한없음_예외발생() {
            // given
            Member member = members.get(2);
            Long playlistId = playlistIds.get(0);

            // when then
            assertThatThrownBy(() -> playlistService.updatePlaylistTitleAndImage(member, playlistId, playlistEdit))
                    .isInstanceOf(RestApiException.class);

            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");
        }
    }

    @Nested
    class AddTrackToPlaylistTest {

        @Test
        void 플레이리스트_트랙_추가_성공_0개_있을때() {
            // given
            Track addTrack = tracks.get(0);
            PlaylistRequest.TrackId trackId = PlaylistRequest.TrackId.builder()
                    .trackId(addTrack.getId())
                    .build();
            Long playlistId = playlistIds.get(3);

            // when
            playlistService.addTrackToPlaylist(members.get(1), playlistId, trackId);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 기존 플레이리스트 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목3");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test3.com");

            // 트랙 추가된 정보
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
            assertThat(playlistTracks).hasSize(1);
            assertThat(playlistTracks.get(0).getTrack().getId()).isEqualTo(addTrack.getId());
            assertThat(playlistTracks.get(0).getOrderIndex()).isEqualTo(0);
        }

        @Test
        void 플레이리스트_트랙_추가_성공_1개_이상_있을때() {
            // given
            Track addTrack = tracks.get(5);
            PlaylistRequest.TrackId trackId = PlaylistRequest.TrackId.builder()
                    .trackId(addTrack.getId())
                    .build();
            Long playlistId = playlistIds.get(0);

            // when
            playlistService.addTrackToPlaylist(members.get(0), playlistId, trackId);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 기존 플레이리스트 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");

            // 트랙 추가된 정보
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
            assertThat(playlistTracks).hasSize(4);
            assertThat(playlistTracks.get(3).getTrack().getId()).isEqualTo(addTrack.getId());
            assertThat(playlistTracks.get(3).getOrderIndex()).isEqualTo(3);
        }

        @Test
        void 플레이리스트_트랙_추가_실패_권한없음_예외발생() {
            // given
            Track addTrack = tracks.get(5);
            PlaylistRequest.TrackId trackId = PlaylistRequest.TrackId.builder()
                    .trackId(addTrack.getId())
                    .build();

            // when then
            assertThatThrownBy(() -> playlistService.addTrackToPlaylist(members.get(1), playlistIds.get(0), trackId))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Nested
    class UpdateTrackOrdersTest {

        @Test
        void 받은_데이터로_모든_트랙_순서_변경() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);
            PlaylistRequest.PlaylistOrders playlistOrders = PlaylistRequest.PlaylistOrders.builder()
                    .tracks(List.of(
                            TrackOrder.builder()
                                    .trackId(tracks.get(0).getId())
                                    .orderIndex(2).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(1).getId())
                                    .orderIndex(0).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(2).getId())
                                    .orderIndex(1).build()
                    ))
                    .build();

            // when
            playlistService.updateTrackOrders(member, playlistId, playlistOrders);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);

            assertThat(playlistTracks).hasSize(3);
            assertThat(playlistTracks.get(0).getOrderIndex()).isEqualTo(2);
            assertThat(playlistTracks.get(1).getOrderIndex()).isEqualTo(0);
            assertThat(playlistTracks.get(2).getOrderIndex()).isEqualTo(1);
        }

        @Test
        void 트랙_개수_불일치_예외발생() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);
            PlaylistRequest.PlaylistOrders playlistOrders = PlaylistRequest.PlaylistOrders.builder()
                    .tracks(List.of(
                            TrackOrder.builder()
                                    .trackId(tracks.get(0).getId())
                                    .orderIndex(2).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(1).getId())
                                    .orderIndex(0).build()
                    ))
                    .build();

            // when then
            assertThatThrownBy(() -> playlistService.updateTrackOrders(member, playlistId, playlistOrders))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 트랙_정보_불일치_예외발생() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);
            PlaylistRequest.PlaylistOrders playlistOrders = PlaylistRequest.PlaylistOrders.builder()
                    .tracks(List.of(
                            TrackOrder.builder()
                                    .trackId(tracks.get(0).getId())
                                    .orderIndex(2).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(1).getId())
                                    .orderIndex(0).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(3).getId()) // 플레이리스트에 없는 트랙
                                    .orderIndex(1).build()
                    ))
                    .build();

            // when then
            assertThatThrownBy(() -> playlistService.updateTrackOrders(member, playlistId, playlistOrders))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 트랙_순서_변경_실패_권한없음_예외발생() {
            // given
            Long playlistId = playlistIds.get(0);
            Member member = members.get(1); // 플레이리스트 만든 유저 아님
            PlaylistRequest.PlaylistOrders playlistOrders = PlaylistRequest.PlaylistOrders.builder()
                    .tracks(List.of(
                            TrackOrder.builder()
                                    .trackId(tracks.get(0).getId())
                                    .orderIndex(2).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(1).getId())
                                    .orderIndex(0).build(),
                            TrackOrder.builder()
                                    .trackId(tracks.get(2).getId())
                                    .orderIndex(1).build()
                    ))
                    .build();

            // when then
            assertThatThrownBy(() -> playlistService.updateTrackOrders(member, playlistId, playlistOrders))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Nested
    class DeleteTrackFromPlaylistTest {

        @Test
        void 플레이리스트에서_트랙_삭제_성공() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);
            Long deleteTrackId = tracks.get(1).getId();

            // when
            PlaylistResponse.PlayListId responsePlaylistId = playlistService.deleteTrackFromPlaylist(member, playlistId,
                    deleteTrackId);

            // then
            assertThat(responsePlaylistId.getPlaylistId()).isEqualTo(playlistId);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            assertThat(optionalPlaylist.isPresent()).isTrue();
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(optionalPlaylist.get());
            assertThat(playlistTracks).hasSize(2);
            assertThat(playlistTracks).allSatisfy(pt -> assertThat(pt.getOrderIndex()).isLessThan(3));
            assertThat(playlistTracks).noneMatch(pt -> pt.getTrack().getId().equals(deleteTrackId));
        }

        @Test
        void 플레이리스트에서_트랙_삭제_성공_남은_트랙_순서_정렬() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);
            Long deleteTrackId = tracks.get(1).getId();

            // when
            PlaylistResponse.PlayListId responsePlaylistId = playlistService.deleteTrackFromPlaylist(member, playlistId,
                    deleteTrackId);

            // then
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            assertThat(optionalPlaylist.isPresent()).isTrue();
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(optionalPlaylist.get());

            // orderIndex 값들이 0부터 시작하여 하나씩 증가하는지 확인
            List<Integer> orderIndexes = playlistTracks.stream()
                    .map(PlaylistTrack::getOrderIndex)
                    .sorted()
                    .toList();

            int orderSize = orderIndexes.size();
            List<Integer> list = IntStream.range(0, orderSize)
                    .boxed()
                    .toList();
            assertThat(orderIndexes).containsExactlyElementsOf(list);
        }

        @Test
        void 플레이리스트에서_트랙_삭제_실패_트랙없음_예외발생() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);
            Long nonExistentTrackId = tracks.get(3).getId(); // 플레이리스트에 없는 트랙

            // when then
            assertThatThrownBy(() -> playlistService.deleteTrackFromPlaylist(member, playlistId, nonExistentTrackId))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 플레이리스트에서_트랙_삭제_실패_권한없음_예외발생() {
            // given
            Member member = members.get(1); // 플레이리스트 만든 유저 아님
            Long playlistId = playlistIds.get(0);
            Long trackId = tracks.get(0).getId();

            // when then
            assertThatThrownBy(() -> playlistService.deleteTrackFromPlaylist(member, playlistId, trackId))
                    .isInstanceOf(RestApiException.class);
        }
    }
}
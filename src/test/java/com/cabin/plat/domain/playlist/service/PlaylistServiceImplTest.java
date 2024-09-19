package com.cabin.plat.domain.playlist.service;

import static org.assertj.core.api.Assertions.*;

import com.cabin.plat.domain.member.entity.*;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload.TrackOrder;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.entity.Playlist;
import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import com.cabin.plat.domain.playlist.repository.PlaylistRepository;
import com.cabin.plat.domain.playlist.repository.PlaylistTrackRepository;
import com.cabin.plat.domain.track.entity.Location;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.repository.LocationRepository;
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
class PlaylistServiceImplTest {

    @Autowired
    private PlaylistService playlistService;

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
        Track track0 = new Track(null, members.get(0), locations.get(0), "isrc1", "기숙사에서 한곡", "https://testimage1.com");
        Track track1 = new Track(null, members.get(0), locations.get(1), "isrc2", "버거킹마을 공연 최애 노래", "https://testimage2.com");
        Track track2 = new Track(null, members.get(1), locations.get(2), "isrc3", "아카데미는 이 노래지", "https://testimage3.com");
        Track track3 = new Track(null, members.get(1), locations.get(3), "isrc4", "리버스아카데미 노래", "https://testimage4.com");
        Track track4 = new Track(null, members.get(2), locations.get(4), "isrc5", "영일대는 이노래지", "https://testimage5.com");
        Track track5 = new Track(null, members.get(2), locations.get(5), "isrc6", "황리단길과 어울리는", "https://testimage5.com");

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
                        ,TrackOrder.builder()
                                .trackId(tracks.get(1).getId())
                                .orderIndex(1)
                                .build()
                        ,TrackOrder.builder()
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
                        ,TrackOrder.builder()
                                .trackId(tracks.get(4).getId())
                                .orderIndex(1)
                                .build()
                        ,TrackOrder.builder()
                                .trackId(tracks.get(5).getId())
                                .orderIndex(2)
                                .build()
                ))
                .build();

        return List.of(playlistUpload0, playlistUpload1);
    }

    private List<Long> createTestPlaylists(List<Member> members, List<PlaylistUpload> playlistUploads) {
        Long playlistId0 = playlistService.addPlaylist(members.get(0), playlistUploads.get(0)).getPlaylistId();
        Long playlistId1 = playlistService.addPlaylist(members.get(1), playlistUploads.get(1)).getPlaylistId();
        return List.of(playlistId0, playlistId1);
    }

    @Nested
    class AddPlaylist {

        @Test
        void 플레이리스트_생성_테스트() {
            // given
            Long playlistId = playlistIds.get(0);

            // when
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId); // 저장된 플레이리스트 확인

            // then
            assertThat(playlistId).isNotNull();
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");
            assertThat(playlist.getPlaylistTracks().get(0).getId()).isEqualTo(tracks.get(0).getId());
            assertThat(playlist.getPlaylistTracks().get(0).getOrderIndex()).isEqualTo(0);
            assertThat(playlist.getPlaylistTracks().get(1).getId()).isEqualTo(tracks.get(1).getId());
            assertThat(playlist.getPlaylistTracks().get(1).getOrderIndex()).isEqualTo(1);
        }

        @Test
        void 플레이리스트_트랙_관계_테이블_생성_테스트() {
            // given
            Optional<PlaylistTrack> optionalPlaylistTrack1 = playlistTrackRepository.findById(1L);
            Optional<PlaylistTrack> optionalPlaylistTrack2 = playlistTrackRepository.findById(2L);

            // when
            assertThat(optionalPlaylistTrack1.isPresent()).isTrue();
            assertThat(optionalPlaylistTrack2.isPresent()).isTrue();
            PlaylistTrack playlistTrack1 = optionalPlaylistTrack1.get();
            PlaylistTrack playlistTrack2 = optionalPlaylistTrack2.get();

            // then
            assertThat(playlistTrack1.getPlaylist().getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlistTrack2.getPlaylist().getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlistTrack1.getPlaylist().getPlaylistImageUrl()).isEqualTo("https://test0.com");
            assertThat(playlistTrack2.getPlaylist().getPlaylistImageUrl()).isEqualTo("https://test0.com");
            assertThat(playlistTrack1.getTrack().getIsrc()).isEqualTo("isrc1");
            assertThat(playlistTrack2.getTrack().getIsrc()).isEqualTo("isrc2");
            assertThat(playlistTrack1.getOrderIndex()).isEqualTo(0);
            assertThat(playlistTrack2.getOrderIndex()).isEqualTo(1);
            assertThat(playlistTrack1.getPlaylist().getId()).isEqualTo(playlistIds.get(0));
            assertThat(playlistTrack2.getPlaylist().getId()).isEqualTo(playlistIds.get(1));
        }
    }

    @Test
    void getPlaylistsTest() {
        // given
        Member member = members.get(0);

        // when
        PlaylistResponse.Playlists playlists = playlistService.getPlaylists(member, 0, 20);
        PlaylistResponse.Playlists.PlaylistInfo playlistInfo0 = playlists.getPlaylists().get(0);
        PlaylistResponse.Playlists.PlaylistInfo playlistInfo1 = playlists.getPlaylists().get(1);

        // then
        assertThat(playlists.getPlaylists().size()).isEqualTo(2);

        assertThat(playlistInfo0.getPlaylistId()).isEqualTo(playlistIds.get(0));
        assertThat(playlistInfo0.getTitle()).isEqualTo("플레이리스트 제목0");
        assertThat(playlistInfo0.getPlaylistImageUrl()).isEqualTo("https://test0.com");
        assertThat(playlistInfo0.getUploaderNicknames()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(List.of(members.get(0).getNickname(), members.get(1).getNickname()));

        assertThat(playlistInfo1.getPlaylistId()).isEqualTo(playlistIds.get(1));
        assertThat(playlistInfo1.getTitle()).isEqualTo("플레이리스트 제목1");
        assertThat(playlistInfo1.getPlaylistImageUrl()).isEqualTo("https://test1.com");
        assertThat(playlistInfo1.getUploaderNicknames()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(List.of(members.get(1).getNickname(), members.get(2).getNickname()));
    }

    @Test
    void getSearchedPlaylistsTest() {
        // given
        Member member = members.get(0);

        // when
        PlaylistResponse.Playlists playlists = playlistService.getSearchedPlaylists(member, "제목0", 0, 20);
        PlaylistResponse.Playlists.PlaylistInfo playlistInfo0 = playlists.getPlaylists().get(0);

        // then
        assertThat(playlists.getPlaylists().size()).isEqualTo(1);

        assertThat(playlistInfo0.getPlaylistId()).isEqualTo(playlistIds.get(0));
        assertThat(playlistInfo0.getTitle()).isEqualTo("플레이리스트 제목0");
        assertThat(playlistInfo0.getPlaylistImageUrl()).isEqualTo("https://test0.com");
        assertThat(playlistInfo0.getUploaderNicknames()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(List.of(members.get(0).getNickname(), members.get(1).getNickname()));
    }

    @Nested
    class DeletePlaylistTests {

        @Test
        void 플레이리스트_삭제_성공() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);

            // when
            playlistService.deletePlaylist(member, playlistId);
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            // then
            assertThat(playlist.isPresent()).isFalse();
        }

        @Test
        void 플레이리스트_삭제_실패_권한없음() {
            // given
            Member member = members.get(1);
            Long playlistId = playlistIds.get(0);

            // when
            playlistService.deletePlaylist(member, playlistId);
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            // then
            assertThat(playlist.isPresent()).isTrue();
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

        private PlaylistRequest.PlaylistUpload newPlaylistUpload = PlaylistUpload.builder()
                .title("플레이리스트 제목2")
                .playlistImageUrl("https://test2.com")
                .tracks(List.of(
                        TrackOrder.builder()
                                .trackId(tracks.get(4).getId())
                                .orderIndex(0)
                                .build()
                        ,TrackOrder.builder()
                                .trackId(tracks.get(5).getId())
                                .orderIndex(1)
                                .build()
                ))
                .build();

        @Test
        void 플레이리스트_수정_성공() {
            // given
            Member member = members.get(0);
            Long playlistId = playlistIds.get(0);

            // when
            playlistService.updatePlaylist(member, playlistId, newPlaylistUpload);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 변경된 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목2");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test2.com");
            assertThat(playlist.getPlaylistTracks().get(0).getId()).isEqualTo(tracks.get(4).getId());
            assertThat(playlist.getPlaylistTracks().get(0).getOrderIndex()).isEqualTo(0);
            assertThat(playlist.getPlaylistTracks().get(1).getId()).isEqualTo(tracks.get(5).getId());
            assertThat(playlist.getPlaylistTracks().get(1).getOrderIndex()).isEqualTo(1);
        }

        @Test
        void 플레이리스트_수정_실패_권한없음() {
            // given
            Member member = members.get(2);
            Long playlistId = playlistIds.get(0);

            // when
            playlistService.updatePlaylist(member, playlistId, newPlaylistUpload);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 변경안된 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");
            assertThat(playlist.getPlaylistTracks().get(0).getId()).isEqualTo(tracks.get(0).getId());
            assertThat(playlist.getPlaylistTracks().get(0).getOrderIndex()).isEqualTo(0);
            assertThat(playlist.getPlaylistTracks().get(1).getId()).isEqualTo(tracks.get(1).getId());
            assertThat(playlist.getPlaylistTracks().get(1).getOrderIndex()).isEqualTo(1);
        }

        @Test
        void 플레이리스트_수정_실패_권한없음_예외발생() {
            // given
            Member member = members.get(2);
            Long playlistId = playlistIds.get(0);

            // when then
            assertThatThrownBy(() -> playlistService.updatePlaylist(member, playlistId, newPlaylistUpload))
                    .isInstanceOf(RestApiException.class);
        }
    }


    @Nested
    class AddTrackToPlaylistTest {

        @Test
        void 플레이리스트_트랙_추가_성공() {
            // given
            Track addTrack = tracks.get(5);
            PlaylistRequest.TrackId trackId = PlaylistRequest.TrackId.builder()
                    .trackId(addTrack.getId())
                    .build();

            // when
            playlistService.addTrackToPlaylist(members.get(0), playlistIds.get(0), trackId);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistIds.get(0));

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 기존 플레이리스트 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");

            // 트랙 추가된 정보
            assertThat(playlist.getPlaylistTracks().size()).isEqualTo(4);
            assertThat(playlist.getPlaylistTracks().get(3).getId()).isEqualTo(addTrack.getId());
            assertThat(playlist.getPlaylistTracks().get(3).getOrderIndex()).isEqualTo(3);
        }

        @Test
        void 플레이리스트_트랙_추가_실패_권한없음() {
            // given
            Track addTrack = tracks.get(5);
            PlaylistRequest.TrackId trackId = PlaylistRequest.TrackId.builder()
                    .trackId(addTrack.getId())
                    .build();

            // when
            playlistService.addTrackToPlaylist(members.get(1), playlistIds.get(0), trackId);
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistIds.get(0));

            // then
            assertThat(optionalPlaylist.isPresent()).isTrue();
            Playlist playlist = optionalPlaylist.get();

            // 기존 플레이리스트 정보
            assertThat(playlist.getTitle()).isEqualTo("플레이리스트 제목0");
            assertThat(playlist.getPlaylistImageUrl()).isEqualTo("https://test0.com");

            // 트랙 추가 안됨
            assertThat(playlist.getPlaylistTracks().size()).isEqualTo(3);
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
}
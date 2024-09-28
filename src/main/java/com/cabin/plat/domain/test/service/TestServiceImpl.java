package com.cabin.plat.domain.test.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.entity.StreamType;
import com.cabin.plat.domain.member.service.MemberService;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.service.PlaylistService;
import com.cabin.plat.domain.track.dto.TrackRequest.TrackUpload;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.service.TrackService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final MemberService memberService;
    private final TrackService trackService;
    private final PlaylistService playlistService;
    private Random random = new Random();

    @Override
    public void addMockData(Member member) {
        memberService.updateAvatarUrl(member, randomAvatarUrl());   // 랜덤 아바타 URL
        memberService.updateNickname(member, randomNickname());     // 랜덤 닉네임
        memberService.updateStreamType(member, StreamType.APPLE_MUSIC);

        TrackResponse.TrackDetailList trackFeed = trackService.getTrackFeeds(member, 0, 100); // Assuming max 100 tracks
        addRandomTracks(member);
        randomLikeTracksFromFeed(member, trackFeed);
        List<Long> trackFeedIds = trackFeed.getTrackDetails().stream().map(TrackResponse.TrackDetail::getTrackId).toList();
        createRandomPlaylists(member, trackFeedIds);
    }

    private void addRandomTracks(Member member) {
        for (int i = 0; i < 5; i++) {
            TrackUpload trackUpload = TrackUpload.builder()
                    .isrc(randomIsrc())
                    .imageUrl(randomImageUrl())
                    .content(randomContent())
                    .latitude(randomLatitude())
                    .longitude(randomLongitude())
                    .build();

            trackService.addTrack(member, trackUpload);
        }
    }

    private void randomLikeTracksFromFeed(Member member, TrackResponse.TrackDetailList trackFeed) {
        List<TrackResponse.TrackDetail> allTracks = trackFeed.getTrackDetails();
        int halfSize = allTracks.size() / 2;

        List<TrackResponse.TrackDetail> shuffledTracks = new ArrayList<>(allTracks);
        java.util.Collections.shuffle(shuffledTracks);

        for (int i = 0; i < halfSize; i++) {
            TrackResponse.TrackDetail track = shuffledTracks.get(i);
            trackService.likeTrack(member, track.getTrackId(), true);
        }
    }

    private void createRandomPlaylists(Member member, List<Long> tracks) {
        if (tracks.isEmpty()) return;
        // 랜덤 트랙 수 결정 (최소 3, 최대 10개)
        int randomTrackSize = Math.min(random.nextInt(Math.min(tracks.size(), 8)) + 3, tracks.size());
        List<Long> shuffledTracks = new ArrayList<>(tracks);
        java.util.Collections.shuffle(shuffledTracks);

        for (int playlistCount = 0; playlistCount < 3; playlistCount++) {
            String playlistTitle = "테스트 플레이리스트 " + random.nextInt(1000);
            String playlistImageUrl = randomImageUrl();

            List<PlaylistRequest.TrackOrder> trackOrders = new ArrayList<>();
            for (int i = 0; i < randomTrackSize; i++) {
                trackOrders.add(new PlaylistRequest.TrackOrder(shuffledTracks.get(i), i));
            }

            PlaylistRequest.PlaylistUpload playlistUpload = new PlaylistRequest.PlaylistUpload(playlistTitle,
                    playlistImageUrl, trackOrders);
            playlistService.addPlaylist(member, playlistUpload);
        }
    }

    // 랜덤 아바타 URL을 생성하는 함수
    private String randomAvatarUrl() {
        List<String> avatarUrlList = List.of(
                "https://cabin.com/avatar1.jpg",
                "https://cabin.com/avatar2.jpg",
                "https://cabin.com/avatar3.jpg",
                "https://cabin.com/avatar4.jpg",
                "https://cabin.com/avatar5.jpg"
        );
        return avatarUrlList.get(random.nextInt(avatarUrlList.size()));
    }

    // 랜덤 닉네임을 생성하는 함수
    private String randomNickname() {
        return "테스트유저 " + random.nextInt(1000);
    }

    // 유명한 곡들의 ISRC 목록에서 랜덤으로 선택하는 함수
    private String randomIsrc() {
        List<String> isrcList = List.of(
                "USUM71703861",  // Ed Sheeran - Shape of You
                "GBUM72105436",  // Adele - Easy On Me
                "USRC12000699",  // Olivia Rodrigo - Drivers License
                "USQX92103263",  // Lil Nas X - MONTERO (Call Me By Your Name)
                "USUM72025593",  // The Weeknd - Blinding Lights
                "USUM71803359",  // Drake - God's Plan
                "USUM71707106",  // Dua Lipa - New Rules
                "USUM72014685",  // Justin Bieber - Peaches (feat. Daniel Caesar & Giveon)
                "USUM71707109",  // Billie Eilish - Bad Guy
                "GBUM72105945",  // Doja Cat - Say So
                "USUM72025722",  // Post Malone - Circles
                "USUM71906363",  // Travis Scott - Sicko Mode
                "USUM71903215",  // Maroon 5 - Sugar
                "USUM71902777",  // Halsey - Without Me
                "USUM72021836",  // BTS - Dynamite
                "USUM71902390",  // Taylor Swift - Blank Space
                "USUM71906705",  // Khalid - Talk
                "USUM71707091",  // Camila Cabello - Havana
                "USUM71805768",  // J Balvin - Mi Gente
                "USUM71901834",  // Charlie Puth - Attention
                "USUM71905467"   // Lizzo - Truth Hurts
        );
        return isrcList.get(random.nextInt(isrcList.size()));
    }

    // 이미지 URL 목록에서 랜덤으로 선택하는 함수
    private String randomImageUrl() {
        List<String> imageUrlList = List.of(
                "https://plat-s3-bucket.s3.ap-northeast-2.amazonaws.com/image/D7DA4601-B705-443A-BC4E-3CEDAF652FE7_1_105_c_ef694f21-936b-42eb-b1fc-e2660efd2900.jpeg",
                "https://plat-s3-bucket.s3.ap-northeast-2.amazonaws.com/image/IMG_4620_ffce6548-ee2a-4506-a44d-21b7cae02ef5.jpg",
                "https://plat-s3-bucket.s3.ap-northeast-2.amazonaws.com/image/IMG_4560_b70309c4-db28-4703-9561-3bfa5fe5f142.jpeg",
                "https://plat-s3-bucket.s3.ap-northeast-2.amazonaws.com/image/B8045AA4-83C4-45CC-9522-8028DAD690FB_4_5005_c_1e4e7a95-ee23-46a9-a9f7-da859a598e2a.jpeg",
                ""
        );
        return imageUrlList.get(random.nextInt(imageUrlList.size()));
    }

    // 200자 이하의 Content 문구를 랜덤으로 생성하는 함수
    private String randomContent() {
        List<String> contentList = List.of(
                "오랜만에 카페에서 집중하는 시간. 나도 모르게 Ed Sheeran의 'Shape of You'를 들으며 커피 한 잔.",
                "공원에서 산책하다가 귀에 꽂힌 노래, Adele의 'Easy On Me'. 감성에 젖어 버렸다.",
                "드라이브 중에 Olivia Rodrigo의 'Drivers License'가 나와서 혼자 따라 부르기 시작했다. 이 노래는 역시 최고!",
                "해변에서 일몰을 보며 Lil Nas X의 'MONTERO'를 듣고 있으면 정말 다른 세계에 온 기분이다.",
                "야경을 보며 The Weeknd의 'Blinding Lights'를 들으니 기분이 한껏 업된다. 이 분위기에 딱 맞는 노래!",
                "비 내리는 날, 집에서 편하게 The Weeknd를 들으며 커피 한 잔의 여유를 느끼고 있다. 창밖의 빗소리가 나의 하루를 더욱 평화롭게 만들어 준다.",
                "강가에서 Olivia Rodrigo의 'Drivers License'를 들으며 혼자만의 시간을 즐겼다. 이 노래는 내 마음속의 감정을 끄집어낸다.",
                "밤하늘을 보며 Adele의 'Easy On Me'를 들었을 때의 기분은 정말 말로 표현할 수 없었다. 가사가 나의 감정을 모두 대변해 주는 느낌이다.",
                "커피를 마시며 Lil Nas X의 'MONTERO'를 들을 때마다 나는 더욱 창의적이게 되는 것 같다. 이 노래는 언제 들어도 좋다.",
                "저녁 공기를 마시며 혼자만의 시간을 보낸다. The Weeknd의 'Blinding Lights'가 귓가에 울리면서 하루의 피로가 사라지는 기분이다.",
                "운전 중 듣는 Ed Sheeran의 'Shape of You'는 내 인생 최고의 BGM 중 하나다. 이 노래는 나에게 특별한 추억을 선사한다.",
                "드디어 여행을 떠났다. 창밖으로 펼쳐진 풍경은 나를 매료시켰고, 이 노래는 나의 감정을 더욱 깊이 자극했다."
                        + "드라이브 내내 이 노래가 반복 재생되었고, 어느새 나는 그 멜로디에 푹 빠져들었다. 이 순간, 모든 것이 완벽했다. "
                        + "풍경, 날씨, 그리고 그 노래. 이러한 순간이 내 기억 속에 오래도록 남을 것이다. 음악은 때때로 우리를 특별한 곳으로 데려다 준다."
        );

        return contentList.get(random.nextInt(contentList.size()));  // 랜덤 Content 반환
    }

    // 위도 랜덤 생성 함수
    private double randomLatitude() {
        double minLatitude = 36.004500;
        double maxLatitude = 36.016000;
        return minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
    }

    // 경도 랜덤 생성 함수
    private double randomLongitude() {
        double minLongitude = 129.322000;
        double maxLongitude = 129.340000;
        return minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
    }
}

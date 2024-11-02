package com.cabin.plat.domain.test.service;

import static java.util.Collections.shuffle;

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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final MemberService memberService;
    private final TrackService trackService;
    private final PlaylistService playlistService;
    private Random random = new Random();

    @Override
    @Transactional
    public void addMockData(Member member) {
        updateRandomMemberData(member);
        addRandomTracks(member);
        List<Long> trackIds = trackService.getTrackFeeds(member, 0, 100).getTrackDetails().stream()
                .map(TrackResponse.TrackDetail::getTrackId)
                .toList();
        randomLikeTracksFromFeed(member, trackIds);
        createRandomPlaylists(member, trackIds);
    }

    private void updateRandomMemberData(Member member) {
        memberService.updateAvatarUrl(member, randomAvatarUrl());
        memberService.updateNickname(member, randomNickname());
        memberService.updateStreamType(member, StreamType.APPLE_MUSIC);
    }

    private void addRandomTracks(Member member) {
        for (int i = 0; i < 5; i++) {
            TrackUpload trackUpload = TrackUpload.builder()
                    .isrc(randomIsrc())
                    .imageUrl(randomTrackImageUrl())
                    .content(randomContent())
                    .latitude(randomLatitude())
                    .longitude(randomLongitude())
                    .build();

            trackService.addTrack(member, trackUpload);
        }
    }

    private void randomLikeTracksFromFeed(Member member, List<Long> trackIds) {
        int halfSize = trackIds.size() / 2;

        List<Long> shuffledTrackIds = new ArrayList<>(trackIds);
        shuffle(shuffledTrackIds);

        for (int i = 0; i < halfSize; i++) {
            Long trackId = shuffledTrackIds.get(i);
            trackService.likeTrack(member, trackId, true);
        }
    }

    private void createRandomPlaylists(Member member, List<Long> trackIds) {
        int trackSize = trackIds.size();
        if (trackSize < 3) {
            return;
        }

        // 랜덤 플레이리스트 3개 생성
        for (int playlistCount = 0; playlistCount < 3; playlistCount++) {
            String playlistTitle = "테스트 플레이리스트 " + random.nextInt(1000);
            String playlistImageUrl = randomTrackImageUrl();

            // 플레이리스트 안의 트랙과 트랙 개수 랜덤으로 생성 (3 ~ 10, trackSize 이하)
            int maxPlaylistTrackCount = Math.min(trackSize, 11);
            int randomTrackSize = random.nextInt(maxPlaylistTrackCount - 3) + 3;

            List<Long> shuffledTracks = new ArrayList<>(trackIds);
            shuffle(shuffledTracks);

            List<PlaylistRequest.TrackOrder> trackOrders = new ArrayList<>();
            for (int i = 0; i < randomTrackSize; i++) {
                trackOrders.add(new PlaylistRequest.TrackOrder(shuffledTracks.get(i), i));
            }

            PlaylistRequest.PlaylistUpload playlistUpload = new PlaylistRequest.PlaylistUpload(playlistTitle,
                    playlistImageUrl, trackOrders);
            playlistService.addPlaylist(member, playlistUpload);
        }
    }

    private String randomAvatarUrl() {
        List<String> avatarUrlList = List.of(
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/1_1bf79aa4-6bf4-4fd4-8440-56c93c09624d.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/2_7bff983b-0dd0-4749-b6fd-d0d6e49e61f9.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/3_b9770660-d664-4de9-b32a-82f5bbf3da43.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/4_21641fe6-46d0-4ea9-8bd3-2a38bccf4f67.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/4_21641fe6-46d0-4ea9-8bd3-2a38bccf4f67.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/6_a0f0bb40-f573-4ea8-91d9-4df3f32906d1.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/7_3fd87dd0-faff-471c-9871-fa5fba8bdb58.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/8_6cd38933-7dd7-4180-9242-86ea2571d676.jpg"
        );
        return avatarUrlList.get(random.nextInt(avatarUrlList.size()));
    }

    private String randomNickname() {
        return "테스트유저 " + random.nextInt(1000);
    }

    private String randomIsrc() {
        List<String> isrcList = List.of(
                "GBAYE0500605", // Fix you - Coldplay
                "KRA382001452", // Flowering - LUCY
                "KRMIM2210467", // Abnormal Climate - GIRIBOY
                "KRA492101385", // Calibrate - Ha Hyun Sang
                "KRA381701433", // Like it - Yoon Jong Shin
                "JPR652100061" // odoriko - Vaundy
        );
        return isrcList.get(random.nextInt(isrcList.size()));
    }

    private String randomTrackImageUrl() {
        List<String> imageUrlList = List.of(
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/601b22e6-cf2b-4338-be16-d6d8c3698805_3df31265-0154-4879-9198-f91513a37177.jpg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/track1_fd3d8514-9652-4309-8e9b-59efcfdd241f.jpg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/track2_0e496b5f-1f9f-425e-84eb-616bf60ff88b.jpg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/track3_d7550854-24ca-4891-ab46-67d48f1e8180.jpeg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/track4_ae417501-34dc-4c99-8e38-9fd4fdd6836a.jpg",
                "https://plat-bucket.s3.ap-northeast-2.amazonaws.com/image/track7_2af2cd6a-ebaf-445a-97f0-65852bce8820.jpg"
        );
        return imageUrlList.get(random.nextInt(imageUrlList.size()));
    }

    private String randomContent() {
        List<String> contentList = List.of(
                "오랜만에 카페에서 집중하는 시간. 나도 모르게 카페에 어울리는 노래를 들으며 커피 한 잔.",
                "공원에서 산책하다가 귀에 꽂힌 노래, 감성에 젖어 버렸다.",
                "드라이브 중에 이 노래가 나와서 혼자 따라 부르기 시작했다. 이 노래는 역시 최고!",
                "해변에서 일몰을 보며 이 노래를 듣고 있으면 정말 다른 세계에 온 기분이다.",
                "야경을 보며 이 노래를 들으니 기분이 한껏 업된다. 이 분위기에 딱 맞는 노래!",
                "비 내리는 날, 집에서 편하게 노래를 들으며 커피 한 잔의 여유를 느끼고 있다. 창밖의 빗소리가 나의 하루를 더욱 평화롭게 만들어 준다.",
                "투썸에서 노래를 들으며 혼자만의 시간을 즐겼다. 이 노래는 내 마음속의 감정을 끄집어낸다.",
                "밤하늘을 보며 이 노래를 들었을 때의 기분은 정말 말로 표현할 수 없었다. 가사가 나의 감정을 모두 대변해 주는 느낌이다.",
                "커피를 마시며 이 노래를 들을 때마다 나는 더욱 창의적이게 되는 것 같다. 이 노래는 언제 들어도 좋다.",
                "저녁 공기를 마시며 혼자만의 시간을 보낸다. 이 노래가 귓가에 울리면서 하루의 피로가 사라지는 기분이다.",
                "운전 중 듣는 이 노래는 내 인생 최고의 BGM 중 하나다. 이 노래는 나에게 특별한 추억을 선사한다.",
                "드디어 여행을 떠났다. 창밖으로 펼쳐진 풍경은 나를 매료시켰고, 이 노래는 나의 감정을 더욱 깊이 자극했다."
                        + "드라이브 내내 이 노래가 반복 재생되었고, 어느새 나는 그 멜로디에 푹 빠져들었다. 이 순간, 모든 것이 완벽했다. "
                        + "풍경, 날씨, 그리고 그 노래. 이러한 순간이 내 기억 속에 오래도록 남을 것이다. 음악은 때때로 우리를 특별한 곳으로 데려다 준다."
        );

        return contentList.get(random.nextInt(contentList.size()));  // 랜덤 Content 반환
    }

    private double randomLatitude() {
        double minLatitude = 36.004500;
        double maxLatitude = 36.016000;
        return minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
    }

    private double randomLongitude() {
        double minLongitude = 129.322000;
        double maxLongitude = 129.346000;
        return minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
    }
}

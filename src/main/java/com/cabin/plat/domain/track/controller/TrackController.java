package com.cabin.plat.domain.track.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.service.TrackService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")
@Tag(name = "트랙 API", description = "트랙 관련 API 입니다.")
public class TrackController {
    private final TrackService trackService;

    @Operation(summary = "트랙 맵 조회", description = "시작점과 끝점의 위도, 경도를 받아서 좌표값 범위 내부의 모든 트랙의 (아이디, isrc, 좋아요 여부, 위도, 경도)정보를 반환한다.")
    @GetMapping("/map")
    public BaseResponse<TrackResponse.TrackMapList> getTracksByLocation(@AuthMember Member member,
                                                                       @RequestParam double startLatitude,
                                                                       @RequestParam double startLongitude,
                                                                       @RequestParam double endLatitude,
                                                                       @RequestParam double endLongitude) {
        return BaseResponse.onSuccess(trackService.getTracksByLocation(
                member,
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude
        ));
    }

    @Operation(summary = "트랙 디테일 조회", description = "트랙의 아이디로 트랙의 디테일한 정보를 조회합니다. "
            + "locationString : 장소 이름 (없으면 빈 문자열)  "
            + "address : 주소  "
            + "imageUrl : 이미지 주소 (없으면 빈 문자열)  "
            + "context: : 본문  "
            + "likeCount: 트랙의 총 좋아요 개수  "
            + "isLiked : 사용자가 좋아요를 눌렀는지 여부")
    @GetMapping("/{track-id}")
    public BaseResponse<TrackResponse.TrackDetail> getTrackById(@AuthMember Member member, @PathVariable("track-id") String trackId) {
        return BaseResponse.onSuccess(trackService.getTrackById(member, trackId));
    }

    @Operation(summary = "트랙 좋아요 표시", description = "트랙에 사용자의 좋아요를 표시한다. idLiked 가 True 면 좋아요, False 면 좋아요 취소 이다.")
    @PostMapping("/{track-id}/like")
    public BaseResponse<TrackResponse.TrackId> likeTrack(@AuthMember Member member, @PathVariable("track-id") String trackId, @RequestBody TrackRequest.TrackLike trackLike) {
        return BaseResponse.onSuccess(trackService.likeTrack(member, trackId, trackLike.getIsLiked()));
    }

    @Operation(summary = "트랙 게시", description = "트랙 (게시물)을 게시한다.")
    @PostMapping("/")
    public BaseResponse<TrackResponse.TrackId> addTrack(@AuthMember Member member, @RequestBody TrackRequest.TrackUpload trackUpload) {
        return BaseResponse.onSuccess(trackService.addTrack(member, trackUpload));
    }

    @Operation(summary = "트랙 피드 조회", description = "트랙의 피드를 모두 조회한다.")
    @GetMapping("/feeds")
    public BaseResponse<TrackResponse.TrackDetailList> getTrackFeeds(@AuthMember Member member) {
        return BaseResponse.onSuccess(trackService.getTrackFeeds(member));
    }

    @Operation(summary = "트랙 신고", description = "트랙을 신고한다.")
    @PostMapping("/{track-id}/report")
    public BaseResponse<TrackResponse.ReportId> reportTrack(@AuthMember Member member, @PathVariable("track-id") String trackId) {
        return BaseResponse.onSuccess(trackService.reportTrack(member, trackId));
    }
}

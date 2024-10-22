package com.cabin.plat.domain.track.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.service.TrackService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    public BaseResponse<TrackResponse.TrackMapList> getTracksByLocation(
            @AuthMember Member member,
            @Parameter(description = "시작 위도 값", example = "36.016512")
            @RequestParam double startLatitude,
            @Parameter(description = "시작 경도 값", example = "129.321285")
            @RequestParam double startLongitude,
            @Parameter(description = "끝 위도 값", example = "36.012527")
            @RequestParam double endLatitude,
            @Parameter(description = "끝 경도 값", example = "129.328229")
            @RequestParam double endLongitude) {
        return BaseResponse.onSuccess(trackService.getTracksByLocation(
                member,
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude
        ));
    }

    @Operation(summary = "트랙 디테일 조회", description = "트랙의 아이디로 트랙의 디테일한 정보를 조회합니다.")
    @GetMapping("/{trackId}")
    public BaseResponse<TrackResponse.TrackDetail> getTrackById(@AuthMember Member member, @PathVariable("trackId") Long trackId) {
        return BaseResponse.onSuccess(trackService.getTrackById(member, trackId));
    }

    @Operation(summary = "트랙 좋아요 표시", description = "트랙에 사용자의 좋아요를 표시한다. idLiked 가 True 면 좋아요, False 면 좋아요 취소 이다.")
    @PostMapping("/{trackId}/like")
    public BaseResponse<TrackResponse.TrackId> likeTrack(
            @AuthMember Member member,
            @PathVariable("trackId") Long trackId,
            @RequestBody TrackRequest.TrackLike trackLike) {
        return BaseResponse.onSuccess(trackService.likeTrack(member, trackId, trackLike.getIsLiked()));
    }

    @Operation(summary = "트랙 게시", description = "트랙 (게시물)을 게시한다.")
    @PostMapping
    public BaseResponse<TrackResponse.TrackId> addTrack(@AuthMember Member member, @RequestBody TrackRequest.TrackUpload trackUpload) {
        return BaseResponse.onSuccess(trackService.addTrack(member, trackUpload));
    }

    @Operation(summary = "트랙 피드 조회", description = "트랙의 피드를 조회한다. 페이지네이션을 지원합니다. page 파라미터에 페이지 번호를 입력해주세요.")
    @GetMapping("/feeds")
    public BaseResponse<TrackResponse.TrackDetailList> getTrackFeeds(
            @AuthMember Member member,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return BaseResponse.onSuccess(trackService.getTrackFeeds(member, page, size));
    }

    @Operation(summary = "트랙 삭제", description = "트랙을 삭제한다.")
    @DeleteMapping("/{trackId}")
    public BaseResponse<TrackResponse.TrackId> deleteTrack(
            @AuthMember Member member,
            @PathVariable("trackId") Long trackId) {
        return BaseResponse.onSuccess(trackService.deleteTrack(member, trackId));
    }

    @Operation(summary = "트랙 신고", description = "트랙을 신고한다.")
    @PostMapping("/{trackId}/report")
    public BaseResponse<TrackResponse.ReportId> reportTrack(@AuthMember Member member, @PathVariable("trackId") Long trackId) {
        return BaseResponse.onSuccess(trackService.reportTrack(member, trackId));
    }
}

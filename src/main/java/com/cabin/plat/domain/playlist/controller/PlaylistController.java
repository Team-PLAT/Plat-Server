package com.cabin.plat.domain.playlist.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.service.PlaylistService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
@Tag(name = "플레이리스트 API", description = "플레이리스트 관련 API 입니다.")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Operation(summary = "플레이리스트 생성", description = "플레이리스트를 생성한다.")
    @PostMapping
    public BaseResponse<PlaylistResponse.PlayListId> addPlaylist(
            @AuthMember Member member,
            @RequestBody PlaylistUpload playlistUpload) {

        return BaseResponse.onSuccess(playlistService.addPlaylist(member, playlistUpload));
    }

    @Operation(summary = "플레이리스트 목록 조회", description = "사용자의 모든 플레이리스트를 가져온다. 페이지네이션을 지원합니다. page 파라미터에 페이지 번호를 입력해주세요.")
    @GetMapping()
    public BaseResponse<PlaylistResponse.Playlists> getPlaylists(
            @AuthMember Member member,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return BaseResponse.onSuccess(playlistService.getPlaylists(member, page, size));
    }

    @Operation(summary = "플레이리스트 목록 검색", description = "사용자의 플레이리스트를 제목으로 검색해서 가져온다. 페이지네이션을 지원합니다. page 파라미터에 페이지 번호를 입력해주세요.")
    @GetMapping("/search")
    public BaseResponse<PlaylistResponse.Playlists> getPlaylists(
            @AuthMember Member member,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return BaseResponse.onSuccess(playlistService.getSearchedPlaylists(member, title, page, size));
    }

    @Operation(summary = "플레이리스트 삭제", description = "플레이리스트를 삭제한다.")
    @DeleteMapping("/{playlistId}")
    public BaseResponse<PlaylistResponse.PlayListId> deletePlaylist(
            @AuthMember Member member,
            @PathVariable("playlistId") Long playlistId) {
        return BaseResponse.onSuccess(playlistService.deletePlaylist(member, playlistId));
    }

    @Operation(summary = "플레이리스트 디테일 조회", description = "사용자의 플레이리스트 하나의 정보를 가져온다. 내부 트랙의 정보도 모두 가져온다.")
    @GetMapping("/{playlistId}/detail")
    public BaseResponse<PlaylistResponse.PlaylistDetail> getPlaylistDetail(
            @AuthMember Member member,
            @PathVariable("playlistId") Long playlistId) {

        return BaseResponse.onSuccess(playlistService.getPlaylistDetail(member, playlistId));
    }

    @Operation(summary = "플레이리스트 수정", description = "플레이리스트 사진과 제목과 트랙 순서를 편집한다.")
    @PatchMapping("/{playlistId}")
    public BaseResponse<PlaylistResponse.PlayListId> updatePlaylist(
            @AuthMember Member member,
            @PathVariable("playlistId") Long playlistId,
            @RequestBody PlaylistUpload playlistUpload) {

        return BaseResponse.onSuccess(playlistService.updatePlaylist(member, playlistId, playlistUpload));
    }

    @Operation(summary = "플레이리스트에 트랙 추가", description = "플레이리스트에 트랙을 추가한다.")
    @PostMapping("/{playlistId}")
    public BaseResponse<PlaylistResponse.PlayListId> addTrackToPlaylist(
            @AuthMember Member member,
            @PathVariable("playlistId") Long playlistId,
            @RequestBody PlaylistRequest.TrackId trackId) {

        return BaseResponse.onSuccess(playlistService.addTrackToPlaylist(member, playlistId, trackId.getTrackId()));
    }
}

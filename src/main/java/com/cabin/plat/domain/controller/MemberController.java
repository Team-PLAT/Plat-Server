package com.cabin.plat.domain.controller;

import com.cabin.plat.domain.entity.Member;
import com.cabin.plat.domain.entity.StreamAccountType;
import com.cabin.plat.domain.service.MemberService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestHeader("authorization") String token, @RequestParam("id") Long userId) {
        try {
            Member member = memberService.getMemberById(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("userId", member.getId());
            result.put("nickname", member.getNickname());
            result.put("avatarUrl", member.getAvatar());

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("code", "COMMON200");
            response.put("message", "유저 프로필 정보 요청에 성공했습니다.");
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // TODO: 예외 상황 분리해서 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("code", "USER001");
            errorResponse.put("message", "유저 프로필 조회 실패");

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getMemberStreamAccountType(@RequestHeader("authorization") String token, @RequestParam("id") Long userId) {
        try {
            StreamAccountType streamAccountType = memberService.getStreamAccountTypeById(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("streamAccountType", streamAccountType);

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("code", "COMMON200");
            response.put("message", "유저 스트리밍 계정 정보 요청에 성공했습니다.");
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // TODO: 예외 상황 분리해서 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("code", "USER001");
            errorResponse.put("message", "유저 스트리밍 계정 정보 요청 실패.");

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @PatchMapping("/signout")
    public ResponseEntity<Map<String, Object>> signOut(@RequestHeader("authorization") String token) {
        try {
            Long userId = memberService.extractUserIdFromToken(token);

            memberService.signOut(userId, token);

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("code", "COMMON200");
            response.put("message", "요청에 성공하였습니다.");
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // TODO: 예외 상황 분리해서 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("code", "USER003");
            errorResponse.put("message", "유저 회원 탈퇴 실패");

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    // TODO: 이미지 저장 방식 확정 시 구현
    @PatchMapping("avatar")
    public ResponseEntity<Map<String, Object>> updateAvatar(@RequestHeader("authorization") String token, @RequestBody Map<String, String> request) {
        try {
            Long userId = memberService.extractUserIdFromToken(token);
            String newAvatar = request.get("newAvatar");
            memberService.updateAvatar(userId, newAvatar, token);
            return ResponseEntity.ok(new HashMap<>());
        } catch (RuntimeException e) {
            // TODO: 예외 상황 분리해서 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("code", "USER003");
            errorResponse.put("message", "유저 아바타 변경 실패");

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @PatchMapping("/profile/nickname")
    public ResponseEntity<Map<String, Object>> updateNickname(@RequestHeader("authorization") String token, @RequestBody Map<String, String> request) {
        try {
            Long userId = memberService.extractUserIdFromToken(token);
            String newNickname = request.get("nickname");
            Member updatedMember = memberService.updateNickname(userId, newNickname, token);

            Map<String, Object> result = new HashMap<>();
            result.put("id", updatedMember.getId());
            result.put("nickname", updatedMember.getNickname());
            result.put("avatarUrl", updatedMember.getAvatar());
            result.put("streamAccountType", updatedMember.getStreamAccountType());

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("code", "COMMON200");
            response.put("message", "닉네임 변경에 성공하였습니다.");
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // TODO: 예외 상황 분리해서 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("code", "USER005");
            errorResponse.put("message", "닉네임 변경 실패");

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @PatchMapping("/profile/streaming-type")
    public ResponseEntity<Map<String, Object>> updateStreamingType(@RequestHeader("authorization") String token, @RequestBody Map<String, String> request) {
        try {
            Long userId = memberService.extractUserIdFromToken(token);
            String newStreamAccountType = request.get("streamAccountType");
            Member updatedMember = memberService.updateStreamAccountType(userId, StreamAccountType.fromString(newStreamAccountType), token);

            Map<String, Object> result = new HashMap<>();
            result.put("id", updatedMember.getId());
            result.put("nickname", updatedMember.getNickname());
            result.put("avatarUrl", updatedMember.getAvatar());
            result.put("streamAccountType", updatedMember.getStreamAccountType());

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("code", "COMMON200");
            response.put("message", "유저 스트리밍 계정 정보 변경에 성공하였습니다.");
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // TODO: 예외 상황 분리해서 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("code", "USER006");
            errorResponse.put("message", "유저 스트리밍 계정 정보 변경 실패");

            return ResponseEntity.status(400).body(errorResponse);
        }
    }
}

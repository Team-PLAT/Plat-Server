package com.cabin.plat.domain.test.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.test.service.TestService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @Value("${server.env}")
    private String env;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.serverAddress}")
    private String serverAddress;
    @Value("${serverName}")
    private String serverName;

    @GetMapping("/base-response")
    private BaseResponse<String> baseResponseTestFunc() {
        return BaseResponse.onSuccess("Success Response");
    }

    @GetMapping("/error-handler")
    private BaseResponse<Long> errorHandlerTestFunc(@RequestParam(name = "number") Long number) {
        return BaseResponse.onSuccess(number);
    }

    @GetMapping
    private String printTest() {
        return "TEST!!!";
    }

    @GetMapping("/health-check")
    private BaseResponse<?> healthCheck() {
        Map<String, String> responseData = new TreeMap<>();
        responseData.put("serverName", serverName);
        responseData.put("serverAddress", serverAddress);
        responseData.put("serverPort", serverPort);
        responseData.put("env", env);
        return BaseResponse.onSuccess(responseData);
    }

    @GetMapping("/env")
    private String getEnv() {
        Map<String, String> responseData = new HashMap<>();

        return env;
    }

    @GetMapping("/plat")
    private BaseResponse<String> getPlat() {

        return BaseResponse.onSuccess("Plat");
    }

    @PostMapping("/add-mock-data")
    @Operation(summary = "임시 데이터 추가", description = "사용자 이름과 사진, 트랙 정보와 좋아요, 플레이리스트 정보 모두 랜덤으로 생성된다. 현재 로그인된 유저가 업로드한 정보만 생성되므로 여러 계정으로 로그인 하여 실행하면 더 다양한 데이터가 생성됩니다.")
    private BaseResponse<String> addMockData(@AuthMember Member member) {
        testService.addMockData(member);
        return BaseResponse.onSuccess("Success");
    }
}

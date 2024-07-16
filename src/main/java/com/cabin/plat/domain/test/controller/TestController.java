package com.cabin.plat.domain.test.controller;

import com.cabin.plat.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/tests")
public class TestController {

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
}

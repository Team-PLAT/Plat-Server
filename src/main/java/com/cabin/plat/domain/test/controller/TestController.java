package com.cabin.plat.domain.test.controller;

import com.cabin.plat.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {
    @GetMapping("base-response")
    private BaseResponse<String> testFunc() {
        return BaseResponse.onSuccess("Success Response");
    }

    @GetMapping
    private  String printTest() {
        return "TEST";
    }
}

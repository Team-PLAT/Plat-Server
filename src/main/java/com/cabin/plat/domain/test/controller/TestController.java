package com.cabin.plat.domain.test.controller;

import com.cabin.plat.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {
    @GetMapping("/base-response")
    private BaseResponse<String> baseResponseTestFunc() {
        return BaseResponse.onSuccess("Success Response");
    }

    @GetMapping("/error-handler")
    private BaseResponse<Long> errorHandlerTestFunc(@RequestParam(name = "number") Long number) {
        return BaseResponse.onSuccess(number);
    }

    @GetMapping
    private  String printTest() {
        return "TEST";
    }
}

package com.cabin.plat.domain.address.controller;

import com.cabin.plat.config.AuthMember;
import com.cabin.plat.domain.address.dto.AddressResponse;
import com.cabin.plat.domain.address.service.AddressService;
import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
@Tag(name = "주소 API", description = "주소, 위치 관련 API 입니다.")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "역지오코딩", description = "위도 경도를 받아서 해당 위치의 주소를 반환한다.")
    @GetMapping("/reverse-geocode")
    public BaseResponse<AddressResponse.AddressString> getAddress(
            @AuthMember Member member,
            @Parameter(description = "위도 값", example = "36.014188")
            @RequestParam double latitude,
            @Parameter(description = "경도 값", example = "129.325802")
            @RequestParam double longitude) {
        return BaseResponse.onSuccess(addressService.getAddress(latitude, longitude));
    }
}

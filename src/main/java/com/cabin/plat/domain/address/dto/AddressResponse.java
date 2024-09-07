package com.cabin.plat.domain.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class AddressResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "주소", example = "경상북도 포항시 남구 지곡동")
    public static class AddressString {
        private String address;
    }
}

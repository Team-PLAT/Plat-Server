package com.cabin.plat.domain.address.dto;

import lombok.*;

public class AddressResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressString {
        private String address;
    }
}

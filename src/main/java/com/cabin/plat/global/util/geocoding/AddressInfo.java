package com.cabin.plat.global.util.geocoding;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public record AddressInfo(String area1, String area2, String area3, String buildingName) {
    public String toAddress() {
        List<String> parts = Arrays.asList(area1, area2, area3);
        return String.join(" ", parts.stream()
                .filter(part -> !part.isEmpty())
                .toList());
    }
}

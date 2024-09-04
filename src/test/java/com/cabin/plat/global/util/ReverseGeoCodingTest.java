package com.cabin.plat.global.util;

import static org.assertj.core.api.Assertions.*;

import com.cabin.plat.global.util.geocoding.AddressInfo;
import com.cabin.plat.global.util.geocoding.ReverseGeoCoding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReverseGeoCodingTest {
    @Autowired
    private ReverseGeoCoding reverseGeoCoding;

    @Test
    void 주소O_도로명주소O_건물명O() {
        // given
        double latitude = 36.014188;
        double longitude = 129.325802;

        // when
        AddressInfo addressInfo = reverseGeoCoding.getAddressInfo(latitude, longitude);

        // then
        assertThat(addressInfo.area1()).isEqualTo("경상북도");
        assertThat(addressInfo.area2()).isEqualTo("포항시 남구");
        assertThat(addressInfo.area3()).isEqualTo("지곡동");
        assertThat(addressInfo.buildingName()).isEqualTo("포항공대제1융합관");
    }

    @Test
    void 주소O_도로명주소O_건물명X() {
        // given
        double latitude = 36.030597;
        double longitude = 129.399123;

        // when
        AddressInfo addressInfo = reverseGeoCoding.getAddressInfo(latitude, longitude);

        // then
        assertThat(addressInfo.area1()).isEqualTo("경상북도");
        assertThat(addressInfo.area2()).isEqualTo("포항시 남구");
        assertThat(addressInfo.area3()).isEqualTo("송정동");
        assertThat(addressInfo.buildingName()).isEmpty();
    }

    @Test
    void 주소O_도로명주소X_건물명X() {
        // given
        double latitude = 36.018981;
        double longitude = 129.335739;

        // when
        AddressInfo addressInfo = reverseGeoCoding.getAddressInfo(latitude, longitude);

        // then
        assertThat(addressInfo.area1()).isEqualTo("경상북도");
        assertThat(addressInfo.area2()).isEqualTo("포항시 남구");
        assertThat(addressInfo.area3()).isEqualTo("대잠동");
        assertThat(addressInfo.buildingName()).isEmpty();
    }

    @Test
    void 주소X_도로명주소X_건물명X() {
        // given
        double latitude = 36.051039;
        double longitude = 129.396599;

        // when
        AddressInfo addressInfo = reverseGeoCoding.getAddressInfo(latitude, longitude);

        // then
        assertThat(addressInfo.area1()).isEqualTo("바다");
        assertThat(addressInfo.area2()).isEmpty();
        assertThat(addressInfo.area3()).isEmpty();
        assertThat(addressInfo.buildingName()).isEmpty();
    }
}
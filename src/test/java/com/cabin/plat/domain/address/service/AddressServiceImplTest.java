package com.cabin.plat.domain.address.service;

import static org.assertj.core.api.Assertions.*;

import com.cabin.plat.domain.address.dto.AddressResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AddressServiceImplTest {

    @Autowired
    private AddressService addressService;

    @Test
    void 외부API_주소_받아오기_테스트() {
        // given
        double latitude = 36.014188;
        double longitude = 129.325802;

        AddressResponse.AddressString addressString = addressService.getAddress(latitude, longitude);
        assertThat(addressString.getAddress()).isEqualTo("경상북도 포항시 남구 지곡동");
    }

}
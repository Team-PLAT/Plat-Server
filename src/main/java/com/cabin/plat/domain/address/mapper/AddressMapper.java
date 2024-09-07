package com.cabin.plat.domain.address.mapper;

import com.cabin.plat.domain.address.dto.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressResponse.AddressString toAddressString(
            String address
    ) {
        return AddressResponse.AddressString.builder()
                .address(address)
                .build();
    }
}

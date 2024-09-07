package com.cabin.plat.domain.address.service;

import com.cabin.plat.domain.address.dto.AddressResponse;
import com.cabin.plat.domain.address.dto.AddressResponse.AddressString;
import com.cabin.plat.domain.address.mapper.AddressMapper;
import com.cabin.plat.global.util.geocoding.AddressInfo;
import com.cabin.plat.global.util.geocoding.ReverseGeoCoding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final ReverseGeoCoding reverseGeoCoding;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse.AddressString getAddress(double latitude, double longitude) {
        AddressInfo addressInfo = reverseGeoCoding.getAddressInfo(latitude, longitude);
        return addressMapper.toAddressString(addressInfo.toAddress());
    }
}

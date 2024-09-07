package com.cabin.plat.domain.address.service;

import com.cabin.plat.domain.address.dto.AddressResponse;
import org.springframework.stereotype.Service;

public interface AddressService {
    AddressResponse.AddressString getAddress(double latitude, double longitude);
}

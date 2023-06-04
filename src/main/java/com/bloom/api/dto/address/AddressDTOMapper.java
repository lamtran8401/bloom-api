package com.bloom.api.dto.address;

import com.bloom.api.models.Address;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AddressDTOMapper implements Function<Address, AddressDTO> {
    @Override
    public AddressDTO apply(Address address) {
        return new AddressDTO(
            address.getId(),
            address.getReceiver(),
            address.getPhone(),
            address.getProvince(),
            address.getDistrict(),
            address.getWard(),
            address.getDetail(),
            address.isDefault(),
            address.getUser().getId(),
            address.getCreatedAt(),
            address.getUpdatedAt()
        );
    }
}

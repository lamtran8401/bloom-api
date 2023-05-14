package com.bloom.api.services;

import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.Address;
import com.bloom.api.repositories.AddressRepository;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.responseDTO.RecordDeletedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public Address addAddress(Integer userId, Address address) throws RecordNotFoundException {
        var user = userRepository
            .findById(userId)
            .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + userId + "."));

        if (address.isDefault())
            addressRepository.updateAllDefaultAddressByUserId(userId);

        user.addAddress(address);
        address.setUser(user);
        return address;
    }

    public RecordDeletedResponse deleteAddress(Integer addressId) throws RecordNotFoundException {
        Address address = addressRepository
            .findById(addressId)
            .orElseThrow(() -> new RecordNotFoundException("Address not found with id: " + addressId + "."));
        addressRepository.delete(address);

        return RecordDeletedResponse.builder()
            .message("Delete address successfully.")
            .statusCode(HttpStatus.NO_CONTENT.value())
            .build();
    }

    public List<Address> getAllAddress(Integer userId) throws RecordNotFoundException {
        return addressRepository.findAddressesByUserId(userId);
    }

    @Transactional
    public Address updateAddress(Integer addressId, Address address, Integer userId) {
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + userId + "."));

        Address addressToUpdate = addressRepository
            .findByIdAndUserId(addressId, userId);

        if (address.isDefault())
            addressRepository.updateAllDefaultAddressByUserId(userId);

        addressToUpdate.setAddress(address);

        return addressToUpdate;
    }
}

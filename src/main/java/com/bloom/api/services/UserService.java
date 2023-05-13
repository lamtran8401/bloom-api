package com.bloom.api.services;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.exception.NotFoundException;
import com.bloom.api.exception.UserExistedException;
import com.bloom.api.models.Address;
import com.bloom.api.models.User;
import com.bloom.api.repositories.AddressRepository;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.requestDTO.UpdateInfoRequest;
import com.bloom.api.utils.responseDTO.DeleteAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final MappedDTO mappedDTO;

    public List<UserDTO> getAll() {
        return mappedDTO.mapUsersDTO(userRepository.findAll());
    }

    public void save(User user) {
        var userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists)
            throw new UserExistedException("User already existed with email: " + user.getEmail() + ".");

        userRepository.save(user);
    }

    public UserDTO getById(Integer id) throws NotFoundException {
        return mappedDTO.mapUserDTO(
            userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id + "."))
        );
    }

    public UserDTO getByEmail(String email) throws NotFoundException {
        return mappedDTO.mapUserDTO(
            userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email + "."))
        );
    }

    @Transactional
    public UserDTO update(String email, UpdateInfoRequest req) throws NotFoundException {
        var userToUpdate = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email + "."));

        if (req.getName() != null)
            userToUpdate.setName(req.getName());
        if (req.getEmail() != null)
            userToUpdate.setEmail(req.getEmail());
        if (req.getPhone() != null)
            userToUpdate.setPhone(req.getPhone());
        if (req.getBirthDate() != null)
            userToUpdate.setBirthDate(req.getBirthDate());
        if (req.getGender() != null)
            userToUpdate.setGender(req.getGender());

        return mappedDTO.mapUserDTO(userToUpdate);
    }

    @Transactional
    public Address addAddress(Integer userId, Address address) throws NotFoundException {
        var user = userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId + "."));
        user.addAddress(address);
        address.setUser(user);
        return address;
    }

    public DeleteAddressResponse deleteAddress(Integer addressId) throws NotFoundException {
        Address address = addressRepository
            .findById(addressId)
            .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId + "."));
        addressRepository.delete(address);

        return DeleteAddressResponse.builder()
            .message("Delete address successfully.")
            .statusCode(HttpStatus.NO_CONTENT.value())
            .build();
    }

    public List<Address> getAllAddress(Integer userId) throws NotFoundException {
        return userRepository.findAllAddressByUserId(userId);
    }
}

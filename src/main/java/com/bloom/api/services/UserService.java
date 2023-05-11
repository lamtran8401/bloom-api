package com.bloom.api.services;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.exception.NotFoundException;
import com.bloom.api.exception.UserExistedException;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.requestDTO.UpdateInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MappedDTO mappedDTO;

    public List<UserDTO> getAllUser() {
        return mappedDTO.mapUsersDTO(userRepository.findAll());
    }

    public UserDTO save(User user) {
        var userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists)
            throw new UserExistedException("User already existed with email: " + user.getEmail() + ".");

        return mappedDTO.mapUserDTO(userRepository.save(user));
    }

    public UserDTO getUserById(Integer id) throws NotFoundException {
        return mappedDTO
            .mapUserDTO(userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found.")));
    }

    public UserDTO getUserByEmail(String email) throws NotFoundException {
        return mappedDTO
            .mapUserDTO(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email + ".")));
    }

    public UserDTO updateUser(String email, UpdateInfoRequest req) throws NotFoundException {
        // update user info include name, email, phone, birthDay, gender
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

        return mappedDTO.mapUserDTO(userRepository.save(userToUpdate));
    }
}

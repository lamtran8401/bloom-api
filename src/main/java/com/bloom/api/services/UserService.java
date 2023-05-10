package com.bloom.api.services;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.exception.NotFoundException;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
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

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserDTO getUserById(Integer id) throws NotFoundException {
        return mappedDTO
            .mapUserDTO(userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found.")));
    }
}

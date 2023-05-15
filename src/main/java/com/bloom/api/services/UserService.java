package com.bloom.api.services;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.exception.RecordExistedException;
import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.dto.request.UpdateInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MappedDTO mappedDTO;

    public List<UserDTO> getAll() {
        return mappedDTO.mapUsersDTO(userRepository.findAll());
    }

    public void save(User user) {
        var userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists)
            throw new RecordExistedException("User already existed with email: " + user.getEmail() + ".");

        userRepository.save(user);
    }

    public UserDTO getById(Integer id) throws RecordNotFoundException {
        return mappedDTO.mapUserDTO(
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + id + "."))
        );
    }

    public UserDTO getByEmail(String email) throws RecordNotFoundException {
        return mappedDTO.mapUserDTO(
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new RecordNotFoundException("User not found with email: " + email + "."))
        );
    }

    @Transactional
    public UserDTO update(String email, UpdateInfoRequest req) throws RecordNotFoundException {
        var userToUpdate = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("User not found with email: " + email + "."));

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
}

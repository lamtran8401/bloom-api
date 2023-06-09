package com.bloom.api.services;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.exception.RecordExistedException;
import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.exception.UnauthorizedException;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.dto.request.UpdateInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MappedDTO mappedDTO;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        var userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists)
            throw new RecordExistedException("User already existed with email: " + user.getEmail() + ".");

        userRepository.save(user);
    }

    public User getById(Integer id) throws RecordNotFoundException {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + id + "."));
    }

    public User getByEmail(String email) throws RecordNotFoundException {
        return userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RecordNotFoundException("User not found with email: " + email + "."));
    }

    @Transactional
    public User update(String email, UpdateInfoRequest req) throws RecordNotFoundException {
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

        return userToUpdate;
    }

    public void updatePassword(Integer userId, String oldPassword, String newPassword) throws RecordNotFoundException {
        var userToUpdate = userRepository
            .findById(userId)
            .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + userId + "."));
        if (!verifyOldPassword(userToUpdate, oldPassword))
            throw new UnauthorizedException("Old password is incorrect.");

        String encodedPassword = passwordEncoder.encode(newPassword);
        userToUpdate.setPassword(encodedPassword);
    }

    private boolean verifyOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}

package com.bloom.api.services;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.exception.RecordExistedException;
import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.security.JwtService;
import com.bloom.api.utils.dto.request.AuthenticationRequest;
import com.bloom.api.utils.dto.request.RegistrationRequest;
import com.bloom.api.utils.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MappedDTO mappedDTO;

    public AuthenticationResponse register(RegistrationRequest req) {
        var userExists = userRepository.existsByEmail(req.getEmail());
        if (userExists)
            throw new RecordExistedException("User already existed with email: " + req.getEmail() + ".");

        var user = User.builder()
            .name(req.getName())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .build();
        userRepository.save(user);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(mappedDTO.mapUserDTO(user))
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        var user = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new RecordNotFoundException("User not found with email: " + req.getEmail() + "."));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(mappedDTO.mapUserDTO(user))
            .build();
    }

    public AuthenticationResponse refresh(Integer userId, String refreshToken) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new RecordNotFoundException("User not found with id: " + userId + "."));

        jwtService.isRefreshTokenValid(refreshToken, user);

        var accessToken = jwtService.generateAccessToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(newRefreshToken)
            .user(mappedDTO.mapUserDTO(user))
            .build();
    }
}

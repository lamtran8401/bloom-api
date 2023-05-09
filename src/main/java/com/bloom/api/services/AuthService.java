package com.bloom.api.services;

import com.bloom.api.config.JwtService;
import com.bloom.api.exception.NotFoundException;
import com.bloom.api.exception.UserExistException;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.AuthenticationRequest;
import com.bloom.api.utils.AuthenticationResponse;
import com.bloom.api.utils.RegistrationRequest;
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

    public AuthenticationResponse register(RegistrationRequest req) {
        var userExists = userRepository.existsByEmail(req.getEmail());
        if (userExists)
            throw new UserExistException("User already existed with email: " + req.getEmail() + ".");

        var user = User.builder()
            .name(req.getName())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        var User = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new NotFoundException("User not found with email: " + req.getEmail() + "."));
        var jwtToken = jwtService.generateToken(User);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public void logout() {

    }
}

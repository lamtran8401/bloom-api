package com.bloom.api.services;

import com.bloom.api.config.JwtService;
import com.bloom.api.enums.Role;
import com.bloom.api.models.User;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.AuthenticationRequest;
import com.bloom.api.utils.AuthenticationResponse;
import com.bloom.api.utils.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            throw new RuntimeException("User already exists");
        
        var user = User.builder()
            .name(req.getName())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .role(Role.USER)
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
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(User);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public void logout() {

    }
}

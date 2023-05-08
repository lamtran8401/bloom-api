package com.bloom.api.dev;

import com.bloom.api.enums.Role;
import com.bloom.api.models.User;
import com.bloom.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<User> users = List.of(
            User.builder()
                .name("User 1")
                .email("user01@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build(),
            User.builder()
                .name("User 2")
                .email("user02@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build(),
            User.builder()
                .name("User 3")
                .email("user03@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build(),
            User.builder()
                .name("User 4")
                .email("user04@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build(),
            User.builder()
                .name("User 5")
                .email("user05@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build()
        );

        users.forEach(userService::save);
        logger.info("Loaded users.");
    }
}

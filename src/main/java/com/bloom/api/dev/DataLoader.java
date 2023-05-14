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

@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<User> users = List.of(
            User.builder()
                .name("Admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.ADMIN)
                .build(),
            User.builder()
                .name("Lam Tran")
                .email("lamlol01@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build()
        );


        users.forEach(userService::save);
        logger.info("Loaded users.");
    }
}

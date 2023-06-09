package com.bloom.api.dto.user;

import com.bloom.api.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getBirthDate(),
            user.getGender() != null ? user.getGender().name() : null,
            user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().get(),
            user.isEmailVerified(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}

package com.bloom.api.dto;

import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.dto.user.UserDTOMapper;
import com.bloom.api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MappedDTO {
    private final UserDTOMapper userDTOMapper;

    public List<UserDTO> mapUsersDTO(List<User> users) {
        return users.stream()
            .map(userDTOMapper)
            .collect(Collectors.toList());
    }

    public UserDTO mapUserDTO(User user) {
        return userDTOMapper.apply(user);
    }
}

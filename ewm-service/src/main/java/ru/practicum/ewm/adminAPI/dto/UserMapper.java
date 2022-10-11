package ru.practicum.ewm.adminAPI.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.User;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getName());
    }

    public User toUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getEmail(),
                userDto.getName());
    }
}

package ru.practicum.ewm.common.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getName());
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getEmail(),
                userDto.getName());
    }
}

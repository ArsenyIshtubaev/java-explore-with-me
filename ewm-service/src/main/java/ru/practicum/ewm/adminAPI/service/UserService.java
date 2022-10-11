package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.adminAPI.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findById(long userId);

    List<UserDto> findAll();

    UserDto save(UserDto userDto);

    UserDto update(long userId, UserDto userDto);

    void deleteById(long userId);

}

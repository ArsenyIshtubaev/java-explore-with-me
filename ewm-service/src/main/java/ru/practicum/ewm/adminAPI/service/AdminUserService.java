package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.common.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    List<UserDto> findAllById(List<Long> ids);

    List<UserDto> findAllWithoutID(int from, int size);

    UserDto save(UserDto userDto);

    void deleteById(long userId);

}

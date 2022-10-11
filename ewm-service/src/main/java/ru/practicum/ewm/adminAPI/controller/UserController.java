package ru.practicum.ewm.adminAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.dto.UserDto;
import ru.practicum.ewm.adminAPI.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Получен запрос к эндпоинту: '{} {}', Пользователь: Имя: {} и Email: {}", "POST", "/users",
                userDto.getName(), userDto.getEmail());
        return userService.save(userDto);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable long id) {
        log.info("GET user id={}", id);
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteById(id);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id, @RequestBody UserDto userDto) {
        log.info("PATCH user id={}", id);
        return userService.update(id, userDto);
    }
}

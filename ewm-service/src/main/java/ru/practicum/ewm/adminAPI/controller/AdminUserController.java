package ru.practicum.ewm.adminAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.service.AdminUserService;
import ru.practicum.ewm.common.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        if (ids != null && !ids.isEmpty()) {
            return adminUserService.findAllById(ids);
        } else {
            return adminUserService.findAllWithoutID(from, size);
        }
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("Get request: '{} {}', User: Name: {} and Email: {}", "POST", "/users",
                userDto.getName(), userDto.getEmail());
        return adminUserService.save(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        adminUserService.deleteById(id);
    }

}

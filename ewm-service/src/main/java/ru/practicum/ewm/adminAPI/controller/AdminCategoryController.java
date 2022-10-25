package ru.practicum.ewm.adminAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.dto.CategoryDto;
import ru.practicum.ewm.adminAPI.dto.UserDto;
import ru.practicum.ewm.adminAPI.service.AdminCategoryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Autowired
    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос к эндпоинту: '{} {}', Категория: Имя: {}", "POST", "/admin/categories",
                categoryDto.getName());
        return adminCategoryService.save(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public CategoryDto deleteUserById(@PathVariable long catId) {
        log.info("Delete category id={}", catId);
        return adminCategoryService.deleteById(catId);
    }

    @GetMapping("/{catId}")
    public CategoryDto findCategoryById(@PathVariable long catId) {
        log.info("Get category id={}", catId);
       return adminCategoryService.findById(catId);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody CategoryDto categoryDto) {
        log.info("PATCH category id={}, name={}", categoryDto.getId(), categoryDto.getName());
        return adminCategoryService.update(categoryDto);
    }

    @GetMapping
    public List<CategoryDto> findAll() {
        return adminCategoryService.findAll();
    }
}

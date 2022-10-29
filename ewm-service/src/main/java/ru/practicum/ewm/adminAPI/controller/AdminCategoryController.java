package ru.practicum.ewm.adminAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.CategoryDto;
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
        log.info("Get request: '{} {}', Category: Name: {}", "POST", "/admin/categories",
                categoryDto.getName());
        return adminCategoryService.save(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteUserById(@PathVariable long catId) {
        log.info("Delete category id={}", catId);
        adminCategoryService.deleteById(catId);
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

package ru.practicum.ewm.adminAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.service.AdminCategoryService;
import ru.practicum.ewm.common.dto.CategoryDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public CategoryDto create(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Get request: '{} {}', Category: Name: {}", "POST", "/admin/categories",
                categoryDto.getName());
        return adminCategoryService.save(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategoryById(@PathVariable long catId) {
        log.info("Delete category id={}", catId);
        adminCategoryService.deleteById(catId);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("PATCH category id={}, name={}", categoryDto.getId(), categoryDto.getName());
        return adminCategoryService.update(categoryDto);
    }

}

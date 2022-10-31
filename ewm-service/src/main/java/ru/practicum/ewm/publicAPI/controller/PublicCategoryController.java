package ru.practicum.ewm.publicAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.publicAPI.service.PublicCategoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> findAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get all category");
        return publicCategoryService.findAll(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable long id) {
        log.info("Get category id={}", id);
        return publicCategoryService.findById(id);
    }
}

package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.adminAPI.dto.CategoryDto;

import java.util.List;

public interface AdminCategoryService {

    CategoryDto findById(long categoryId);

    List<CategoryDto> findAll();

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    CategoryDto deleteById(long categoryId);
}

package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.common.dto.CategoryDto;

import java.util.List;

public interface AdminCategoryService {

    CategoryDto findById(long categoryId);

    List<CategoryDto> findAll();

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(long categoryId);
}

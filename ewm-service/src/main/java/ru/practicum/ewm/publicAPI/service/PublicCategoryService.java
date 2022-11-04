package ru.practicum.ewm.publicAPI.service;

import ru.practicum.ewm.common.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    CategoryDto findById(long id);

    List<CategoryDto> findAll(int from, int size);

}

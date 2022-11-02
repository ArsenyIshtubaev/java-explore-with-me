package ru.practicum.ewm.adminAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.common.dto.CategoryMapper;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Category;
import ru.practicum.ewm.common.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto findById(long categoryId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new StorageException("Category with Id = " + categoryId + " not found")));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new StorageException("Category with Id = " + categoryDto.getId() + " not found"));
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteById(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}

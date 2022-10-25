package ru.practicum.ewm.adminAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.adminAPI.dto.CategoryDto;
import ru.practicum.ewm.adminAPI.dto.CategoryMapper;
import ru.practicum.ewm.exception.StorageException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Autowired
    public AdminCategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto findById(long categoryId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new StorageException("Категории с Id = " + categoryId + " нет в БД")));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new StorageException("Категории с Id = " + categoryDto.getId() + " нет в БД"));
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto deleteById(long categoryId) {
        CategoryDto oldCategoryDto = findById(categoryId);
        log.info(oldCategoryDto.toString());
        categoryRepository.deleteById(categoryId);
        log.info(String.valueOf(categoryId));
        return oldCategoryDto;
    }
}

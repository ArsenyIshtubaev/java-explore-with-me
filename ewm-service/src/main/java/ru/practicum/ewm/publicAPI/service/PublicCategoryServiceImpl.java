package ru.practicum.ewm.publicAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CategoryDto;
import ru.practicum.ewm.common.dto.CategoryMapper;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto findById(long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new StorageException("Категории с Id = " + id + " нет в БД")));
    }

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}

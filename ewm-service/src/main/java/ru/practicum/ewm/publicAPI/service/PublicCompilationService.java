package ru.practicum.ewm.publicAPI.service;

import ru.practicum.ewm.common.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    CompilationDto findById(long id);

    List<CompilationDto> findAll(Boolean pinned, int from, int size);

}

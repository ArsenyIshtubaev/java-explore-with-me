package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteById(long compId);

    void deleteEventById(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void unpin(long compId);

    void pin(long compId);

}

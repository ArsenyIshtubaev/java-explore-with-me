package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto save(NewCompilationDto newCompilationDto);

    void deleteById(long compId);

    void deleteEventById(long compId, long eventId);

    void updateCompilation(long compId, long eventId);

    void deletePinById(long compId);

    void updatePin(long compId);

}

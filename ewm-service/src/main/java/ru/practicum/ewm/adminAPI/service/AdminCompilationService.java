package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.adminAPI.dto.NewCompilationDto;

public interface AdminCompilationService {

    NewCompilationDto save(NewCompilationDto newCompilationDto);

    void deleteById(long compId);

    void deleteEventById(long compId, long eventId);

    void updateCompilation(long compId, long eventId);

    void deletePinById(long compId);

    void updatePin(long compId);

}

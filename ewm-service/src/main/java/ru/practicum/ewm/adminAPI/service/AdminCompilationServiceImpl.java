package ru.practicum.ewm.adminAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.adminAPI.dto.CompilationMapper;
import ru.practicum.ewm.adminAPI.dto.NewCompilationDto;
import ru.practicum.ewm.exception.StorageException;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;


import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {


    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    public AdminCompilationServiceImpl(CompilationRepository compilationRepository, CompilationMapper compilationMapper, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
        this.eventRepository = eventRepository;
    }


    @Override
    public NewCompilationDto save(NewCompilationDto newCompilationDto) {
        Set<Event> eventSet = newCompilationDto.getEvents()
                .stream()
                .map(eventRepository::findById)
                .map(event -> event.orElseThrow(() -> new StorageException("Event not found")))
                .collect(Collectors.toSet());
        return compilationMapper.toNewCompilation(compilationRepository
                .save(compilationMapper.toCompilation(newCompilationDto, eventSet)));
    }

    @Override
    public void deleteById(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventById(long compId, long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);

    }

    @Override
    public void updateCompilation(long compId, long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);

    }

    @Override
    public void deletePinById(long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void updatePin(long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}

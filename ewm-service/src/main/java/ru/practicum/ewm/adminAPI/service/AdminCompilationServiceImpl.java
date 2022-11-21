package ru.practicum.ewm.adminAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.repository.CompilationRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.utills.SearchEventValues;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final SearchEventValues searchEventValues;


    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Set<Event> eventSet = new HashSet<>();
        Set<EventShortDto> eventShortDtos = new HashSet<>();
        if (!newCompilationDto.getEvents().isEmpty()) {
            eventSet = newCompilationDto.getEvents().stream()
                    .map(eventRepository::findById)
                    .map(event -> event.orElseThrow(() -> new StorageException("Event not found")))
                    .collect(Collectors.toSet());
            List<Long> ids = eventSet.stream().map(Event::getId).collect(Collectors.toList());
            HashMap<Long, Long> confirmedRequests = searchEventValues.getConfirmedRequests(ids);
            HashMap<Long, Integer> eventViews = searchEventValues.getEventsViews(ids);
            eventShortDtos = eventSet.stream()
                    .map(event -> EventMapper.toEventShortDto(event,
                            confirmedRequests.get(event.getId()),
                            eventViews.get(event.getId())))
                    .collect(Collectors.toSet());
        }
        Compilation compilation = compilationRepository
                .save(CompilationMapper.toCompilation(newCompilationDto, eventSet));
        return CompilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public void deleteById(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventById(long compId, long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);

    }

    @Override
    @Transactional
    public void addEventInCompilation(long compId, long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void unpin(long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void pin(long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + compId + " not found"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}

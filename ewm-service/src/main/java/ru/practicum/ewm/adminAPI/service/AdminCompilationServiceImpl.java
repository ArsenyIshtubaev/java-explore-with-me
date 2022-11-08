package ru.practicum.ewm.adminAPI.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.repository.CompilationRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import java.time.LocalDateTime;
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
    private final RequestRepository requestRepository;
    private final HitClient hitClient;

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Set<Event> eventSet = new HashSet<>();
        if (newCompilationDto.getEvents() != null) {
            eventSet = newCompilationDto.getEvents().stream()
                    .map(eventRepository::findById)
                    .map(event -> event.orElseThrow(() -> new StorageException("Event not found")))
                    .collect(Collectors.toSet());
        }
        Set<EventShortDto> eventShortDtos = eventSet.stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        getConfirmedRequest(event.getId()),
                        getEventViews(event)))
                .collect(Collectors.toSet());
        return CompilationMapper.toCompilationDto(compilationRepository
                .save(CompilationMapper.toCompilation(newCompilationDto, eventSet)), eventShortDtos);
    }

    private int getConfirmedRequest(long eventId) {
        int confirmedRequest = 0;
        List<Request> requests = requestRepository.findAllByEventIdAndStatus(eventId, State.CONFIRMED);
        if (!requests.isEmpty()) {
            confirmedRequest = requests.size();
        }
        return confirmedRequest;
    }

    private Integer getEventViews(Event event) {
        Gson gson = new Gson();
        Integer views = 0;
        String[] uris = new String[]{"http://ewm-service:8080/events/" + event.getId()};
        ResponseEntity<Object> objectResponseEntity = hitClient
                .getStats(uris,
                        DateTimeMapper.toString(event.getCreatedOn()),
                        DateTimeMapper.toString(LocalDateTime.now()), false);
        if (objectResponseEntity.getStatusCode() == HttpStatus.OK) {
            String responseJson = gson.toJson(objectResponseEntity.getBody());
            Stats stats = gson.fromJson(responseJson, Stats.class);
            views = stats.getStats().stream().findFirst().orElseThrow().getHits();
        }
        return views;
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

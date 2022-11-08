package ru.practicum.ewm.publicAPI.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;
    private final RequestRepository requestRepository;
    private final HitClient hitClient;

    @Override
    public CompilationDto findById(long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new StorageException("Compilation with Id = " + id + " not found"));
        return CompilationMapper.toCompilationDto(compilation, getShortEvents(compilation));
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable)
                    .stream()
                    .map(compilation -> CompilationMapper.toCompilationDto(compilation, getShortEvents(compilation)))
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAll(pageable)
                .stream()
                .map(compilation -> CompilationMapper.toCompilationDto(compilation, getShortEvents(compilation)))
                .collect(Collectors.toList());
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

    private Set<EventShortDto> getShortEvents(Compilation compilation) {
        return compilation.getEvents().stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        getConfirmedRequest(event.getId()),
                        getEventViews(event)))
                .collect(Collectors.toSet());
    }
}

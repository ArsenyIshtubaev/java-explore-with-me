package ru.practicum.ewm.publicAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.CompilationMapper;
import ru.practicum.ewm.common.dto.EventMapper;
import ru.practicum.ewm.common.dto.EventShortDto;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.repository.CompilationRepository;
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
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;
    private final SearchEventValues searchEventValues;


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

    private Set<EventShortDto> getShortEvents(Compilation compilation) {

        Set<EventShortDto> eventShortDtos = new HashSet<>();
        if (!compilation.getEvents().isEmpty()) {
            Set<Event> events = compilation.getEvents();
            List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
            HashMap<Long, Long> confirmedRequests = searchEventValues.getConfirmedRequests(ids);
            HashMap<Long, Integer> eventViews = searchEventValues.getEventsViews(ids);
            eventShortDtos = events.stream()
                    .map(event -> EventMapper.toEventShortDto(event,
                            confirmedRequests.get(event.getId()),
                            eventViews.get(event.getId())))
                    .collect(Collectors.toSet());
        }
        return eventShortDtos;
    }
}

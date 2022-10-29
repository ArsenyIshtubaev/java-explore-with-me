package ru.practicum.ewm.publicAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.repository.CompilationRepository;
import ru.practicum.ewm.common.repository.RequestRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public PublicCompilationServiceImpl(CompilationRepository compilationRepository, CompilationMapper compilationMapper, EventMapper eventMapper, RequestRepository requestRepository, UserMapper userMapper, CategoryMapper categoryMapper) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CompilationDto findById(long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new StorageException("Подборки событий с Id = " + id + " нет в БД"));
        return compilationMapper.toCompilationDto(compilation, getShortEvents(compilation));
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable)
                    .stream()
                    .map(compilation -> compilationMapper.toCompilationDto(compilation, getShortEvents(compilation)))
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAll(pageable)
                .stream()
                .map(compilation -> compilationMapper.toCompilationDto(compilation, getShortEvents(compilation)))
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

    private Set<EventShortDto> getShortEvents(Compilation compilation) {
        return compilation.getEvents().stream()
                .map(event -> eventMapper.toEventShortDto(event,
                        getConfirmedRequest(event.getId()), userMapper))
                .collect(Collectors.toSet());
    }
}

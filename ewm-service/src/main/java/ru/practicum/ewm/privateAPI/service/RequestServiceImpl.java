package ru.practicum.ewm.privateAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.StorageException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.privateAPI.dto.ParticipationRequestDto;
import ru.practicum.ewm.privateAPI.dto.RequestMapper;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestMapper requestMapper, UserRepository userRepository, EventRepository eventRepository, RequestRepository requestRepository) {
        this.requestMapper = requestMapper;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<ParticipationRequestDto> findAll(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto save(long userId, long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        if (event.getInitiator().equals(requester)) {
            throw new ForbiddenException("initiator of the event cannot add a request to participate in his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("the event can not to have status canceled or pending");
        }
        if (event.getParticipantLimit() <= requestRepository.findAllByEventId(eventId).size()) {
            throw new ForbiddenException("the event has reached the limit of requests for participation");
        }
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException("you can't recreate the same request");
        }
        State status = State.CONFIRMED;
        if (event.getRequestModeration()) {
            status = State.PENDING;
        }
        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .status(status)
                .created(LocalDateTime.now())
                .build();
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto update(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new StorageException("Request with Id = " + requestId + " not found"));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        if (request.getRequester().getId() != userId) {
            throw new ForbiddenException("User with Id = " + userId + " don't have requests");
        }
        request.setStatus(State.CANCELED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}

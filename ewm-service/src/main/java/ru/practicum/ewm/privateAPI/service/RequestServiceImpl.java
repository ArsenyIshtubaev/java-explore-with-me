package ru.practicum.ewm.privateAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.ParticipationRequestDto;
import ru.practicum.ewm.common.dto.RequestMapper;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> findAll(long userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto save(long userId, long eventId) {

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException("you can't recreate the same request");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("the event can not to have status canceled or pending");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("initiator of the event cannot add a request to participate in his event");
        }
        if (event.getParticipantLimit() <= requestRepository.findAllByEventId(eventId).size()) {
            throw new ForbiddenException("the event has reached the limit of requests for participation");
        }
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
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
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new StorageException("Request with Id = " + requestId + " not found"));
        if (request.getRequester().getId() != userId) {
            throw new ForbiddenException("User can't cancel someone else's request");
        }
        request.setStatus(State.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}

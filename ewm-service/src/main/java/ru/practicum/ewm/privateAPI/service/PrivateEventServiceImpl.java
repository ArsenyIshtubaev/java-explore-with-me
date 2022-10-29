package ru.practicum.ewm.privateAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.*;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final RequestMapper requestMapper;
    private final LocationRepository locationRepository;

    @Autowired
    public PrivateEventServiceImpl(EventRepository eventRepository, EventMapper eventMapper,
                                   UserRepository userRepository, UserMapper userMapper,
                                   RequestRepository requestRepository,
                                   CategoryRepository categoryRepository, RequestMapper requestMapper,
                                   LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
        this.requestMapper = requestMapper;
        this.locationRepository = locationRepository;
    }

    @Override
    public EventFullDto findById(long userId, long eventId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with  Id = " + eventId + " not found"));
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event, getConfirmedRequest(eventId), userMapper.toUserDto(initiator));
    }

    @Override
    public List<EventShortDto> findAll(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        return eventRepository.findByInitiatorId(userId, pageable)
                .stream()
                .map(event -> eventMapper.toEventShortDto(event, getConfirmedRequest(event.getId()),
                        userMapper))
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

    @Override
    @Transactional
    public EventFullDto save(long userId, NewEventDto newEventDto) {
        LocalDateTime eventDate = DateTimeMapper.toDateTime(newEventDto.getEventDate());
        if (eventDate.minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("Date of the event cannot be earlier than two hours from the current moment");
        }
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        locationRepository.save(newEventDto.getLocation());
        Event event = eventRepository.save(eventMapper.toEvent(newEventDto, initiator));
        return eventMapper.toEventFullDto(event,
                0,
                userMapper.toUserDto(initiator));
    }

    @Override
    @Transactional
    public EventFullDto update(long userId, UpdateEventRequest updateEventRequest) {

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        long eventId = updateEventRequest.getEventId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("Date of the event cannot be earlier than two hours from the current moment");
        }
        State eventState = event.getState();
        switch (eventState) {
            case CONFIRMED:
                throw new ForbiddenException("Event already confirmed");
            case PUBLISHED:
                throw new ForbiddenException("Event already published");
            case PENDING:
                break;
            case CANCELED:
                event.setState(State.PENDING);
                break;
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new StorageException("Category with  Id = "
                            + updateEventRequest.getCategory() + " not found")));
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime updateEventDate = DateTimeMapper.toDateTime(updateEventRequest.getEventDate());
            if (updateEventDate.minusHours(2).isBefore(LocalDateTime.now())) {
                throw new ForbiddenException("Date of the Update event cannot " +
                        "be earlier than two hours from the current moment");
            }
            event.setEventDate(updateEventDate);
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return eventMapper.toEventFullDto(eventRepository.save(event),
                getConfirmedRequest(eventId), userMapper.toUserDto(initiator));
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(long userId, long eventId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        if (event.getState() != State.PENDING) {
            throw new ForbiddenException("You can only cancel an event in the moderation pending state");
        }
        event.setState(State.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event),
                getConfirmedRequest(eventId), userMapper.toUserDto(initiator));
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestEvents(long userId, long eventId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        if (!initiator.equals(event.getInitiator())) {
            throw new ForbiddenException("User can not to look participation event requests of other users");
        }
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new StorageException("Request with Id = " + reqId + " not found"));
        int participantLimit = event.getParticipantLimit();
        if (!event.getRequestModeration() || participantLimit == 0) {
            throw new ForbiddenException("For the requested operation the conditions are not met.");
        }
        int participantCount = requestRepository.findAllByEventIdAndStatus(eventId, State.CONFIRMED).size();
        if (participantCount >= participantLimit) {
            throw new ForbiddenException("The limit on applications for this event has been reached");
        }
        request.setStatus(State.CONFIRMED);
        if ((participantCount + 1) == participantLimit) {
            requestRepository.findAllByEventIdAndStatus(eventId, State.PENDING)
                    .stream()
                    .peek(request1 -> request1.setStatus(State.CANCELED))
                    .map(requestRepository::save)
                    .collect(Collectors.toList());
        }
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new StorageException("Request with Id = " + reqId + " not found"));

        request.setStatus(State.REJECTED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}

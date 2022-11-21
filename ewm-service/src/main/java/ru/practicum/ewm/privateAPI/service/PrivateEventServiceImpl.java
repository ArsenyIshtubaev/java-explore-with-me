package ru.practicum.ewm.privateAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Category;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.*;
import ru.practicum.ewm.common.utills.DateTimeMapper;
import ru.practicum.ewm.common.utills.SearchEventValues;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final SearchEventValues searchEventValues;

    @Override
    public EventFullDto findById(long userId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId +
                        " and initiatorId = " + userId + " not found"));
        return EventMapper.toEventFullDto(event,
                searchEventValues.getConfirmedRequest(eventId),
                searchEventValues.getEventViews(event));
    }

    @Override
    public List<EventShortDto> findAll(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
        HashMap<Long, Long> confirmedRequests = searchEventValues.getConfirmedRequests(ids);
        HashMap<Long, Integer> eventViews = searchEventValues.getEventsViews(ids);
        return events.stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        confirmedRequests.get(event.getId()),
                        eventViews.get(event.getId())))
                .collect(Collectors.toList());
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
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new StorageException("Category with  Id = "
                        + newEventDto.getCategory() + " not found"));
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, initiator, category));
        return EventMapper.toEventFullDto(event,
                0L,
                searchEventValues.getEventViews(event));
    }

    @Override
    @Transactional
    public EventFullDto update(long userId, UpdateEventRequest updateEventRequest) {

        long eventId = updateEventRequest.getEventId();
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId +
                        " and initiatorId = " + userId + " not found"));
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
        return EventMapper.toEventFullDto(eventRepository.save(event),
                searchEventValues.getConfirmedRequest(eventId),
                searchEventValues.getEventViews(event));
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(long userId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId +
                        " and initiatorId = " + userId + " not found"));
        if (event.getState() != State.PENDING) {
            throw new ForbiddenException("You can only cancel an event in the moderation pending state");
        }
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event),
                searchEventValues.getConfirmedRequest(eventId),
                searchEventValues.getEventViews(event));
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestEvents(long userId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId +
                        " and initiatorId = " + userId + " not found"));
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new StorageException("Request with Id = " + reqId + " not found"));
        Event event = request.getEvent();
        int participantLimit = event.getParticipantLimit();
        if (!event.getRequestModeration() || participantLimit == 0) {
            throw new ForbiddenException("For the requested operation the conditions are not met.");
        }
        int participantCount = requestRepository.findAllByEventIdAndStatus(eventId, State.CONFIRMED).size();
        if (participantCount >= participantLimit) {
            throw new ForbiddenException("The limit on applications for this event has been reached");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("only initiator of the event can to confirm request");
        }
        request.setStatus(State.CONFIRMED);
        if ((participantCount + 1) == participantLimit) {
            requestRepository.findAllByEventIdAndStatus(eventId, State.PENDING)
                    .stream()
                    .peek(request1 -> request1.setStatus(State.CANCELED))
                    .map(requestRepository::save)
                    .collect(Collectors.toList());
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Request request = requestRepository.findByIdAndEventId(reqId, eventId)
                .orElseThrow(() -> new StorageException("Request with Id = " + reqId + " not found"));
        if (request.getEvent().getInitiator().getId() != userId) {
            throw new ForbiddenException("only initiator of the event can to cancel request");
        }
        request.setStatus(State.REJECTED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

}

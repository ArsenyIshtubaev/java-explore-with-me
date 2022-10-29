package ru.practicum.ewm.adminAPI.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventMapper;
import ru.practicum.ewm.common.dto.UpdateEventRequest;
import ru.practicum.ewm.common.dto.UserMapper;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.repository.CategoryRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final UserMapper userMapper;
    private final CategoryRepository categoryRepository;

    public AdminEventServiceImpl(EventRepository eventRepository,
                                 EventMapper eventMapper,
                                 RequestRepository requestRepository,
                                 UserMapper userMapper,
                                 CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
        this.userMapper = userMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<EventFullDto> findAll(Long[] users, State[] states, Long[] categories, String rangeStart,
                                      String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (users != null) {
            conditions.add(event.initiator.id.in(users));
        }
        if (states != null) {
            conditions.add(event.state.in(states));
        }
        if (categories != null) {
            conditions.add(event.category.id.in(categories));
        }
        if (rangeStart != null) {
            conditions.add(event.eventDate.after(DateTimeMapper.toDateTime(rangeStart)));
        }
        if (rangeEnd != null) {
            conditions.add(event.eventDate.before(DateTimeMapper.toDateTime(rangeEnd)));
        }
        BooleanExpression expression = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        return eventRepository.findAll(expression, pageable).stream().map(event1 -> eventMapper.toEventFullDto(event1,
                        getConfirmedRequest(event1.getId()),
                        userMapper.toUserDto(event1.getInitiator())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto update(long eventId, UpdateEventRequest updateEventRequest) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));

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
                getConfirmedRequest(eventId), userMapper.toUserDto(event.getInitiator()));
    }

    @Override
    @Transactional
    public EventFullDto publicationEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with  Id = " + eventId + " not found"));
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return eventMapper.toEventFullDto(eventRepository.save(event),
                getConfirmedRequest(eventId), userMapper.toUserDto(event.getInitiator()));
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with  Id = " + eventId + " not found"));
        event.setState(State.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event),
                getConfirmedRequest(eventId), userMapper.toUserDto(event.getInitiator()));
    }

    private int getConfirmedRequest(long eventId) {
        int confirmedRequest = 0;
        List<Request> requests = requestRepository.findAllByEventIdAndStatus(eventId, State.CONFIRMED);
        if (!requests.isEmpty()) {
            confirmedRequest = requests.size();
        }
        return confirmedRequest;
    }

}

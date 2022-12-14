package ru.practicum.ewm.adminAPI.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventMapper;
import ru.practicum.ewm.common.dto.UpdateEventRequest;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.repository.CategoryRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;
import ru.practicum.ewm.common.utills.SearchEventValues;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final SearchEventValues searchEventValues;

    @Override
    public List<EventFullDto> findAll(List<Long> users, List<State> states, List<Long> categories, String rangeStart,
                                      String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            conditions.add(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            conditions.add(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
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

        List<Event> events = eventRepository.findAll(expression, pageable).toList();
        List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
        HashMap<Long, Long> confirmedRequests = searchEventValues.getConfirmedRequests(ids);
        HashMap<Long, Integer> eventViews = searchEventValues.getEventsViews(ids);
        return events.stream().map(event1 -> EventMapper.toEventFullDto(event1,
                       confirmedRequests.get(event1.getId()),
                        eventViews.get(event1.getId())))
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
        event.setState(State.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event),
                searchEventValues.getConfirmedRequest(eventId),
                searchEventValues.getEventViews(event));
    }

    @Override
    @Transactional
    public EventFullDto publicationEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with  Id = " + eventId + " not found"));
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return EventMapper.toEventFullDto(eventRepository.save(event),
                searchEventValues.getConfirmedRequest(eventId),
                searchEventValues.getEventViews(event));
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with  Id = " + eventId + " not found"));
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event),
                searchEventValues.getConfirmedRequest(eventId),
                searchEventValues.getEventViews(event));
    }

}

package ru.practicum.ewm.publicAPI.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.common.dto.EndpointHit;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventMapper;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;
import ru.practicum.ewm.common.utills.SearchEventValues;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final HitClient hitClient;
    private final SearchEventValues searchEventValues;


    @Override
    public EventFullDto findById(long id, HttpServletRequest request) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new StorageException("Event with Id = " + id + " not found"));
        if (event.getState() != State.PUBLISHED) {
            throw new StorageException("Event with Id = " + id + " don't published");
        }

        hitClient.save(EndpointHit.builder().app("ewm-service")
                .timestamp(DateTimeMapper.toString(LocalDateTime.now()))
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr()).build());

        return EventMapper.toEventFullDto(event,
                searchEventValues.getConfirmedRequest(event.getId()),
                searchEventValues.getEventViews(event));
    }

    @Override
    public List<EventFullDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                      String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                      HttpServletRequest request) {

        Sort sort1 = Sort.unsorted();

        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                sort1 = Sort.by("eventDate");
            }
            if (sort.equals("VIEWS")) {
                sort1 = Sort.by("views");
            }
        }

        Pageable pageable = PageRequest.of(from / size, size, sort1);

        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (text != null) {
            conditions.add(event.description.like(text).or(event.annotation.like(text)));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.add(event.paid.eq(paid));
        }
        if (rangeStart != null) {
            conditions.add(event.eventDate.after(DateTimeMapper.toDateTime(rangeStart)));
        }
        if (rangeEnd != null) {
            conditions.add(event.eventDate.before(DateTimeMapper.toDateTime(rangeEnd)));
        }
        if (rangeStart == null && rangeEnd == null) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        BooleanExpression expression = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        List<Event> result = eventRepository.findAll(expression, pageable).toList();

        List<Long> ids = result.stream().map(Event::getId).collect(Collectors.toList());
        HashMap<Long, Long> confirmedRequests = searchEventValues.getConfirmedRequests(ids);
        HashMap<Long, Integer> eventViews = searchEventValues.getEventsViews(ids);
        if (onlyAvailable) {
            result = result.stream()
                    .filter(event1 -> event1.getParticipantLimit() > confirmedRequests.get(event1.getId()))
                    .collect(Collectors.toList());
        }
        result.stream().peek(event1 -> hitClient.save(EndpointHit.builder().app("ewm-service")
                .timestamp(DateTimeMapper.toString(LocalDateTime.now()))
                .uri(request.getRequestURI() + "/" + event1.getId())
                .ip(request.getRemoteAddr()).build()));

        return result.stream()
                .map(event1 -> EventMapper.toEventFullDto(event1,
                        confirmedRequests.get(event1.getId()),
                        eventViews.get(event1.getId())))
                .collect(Collectors.toList());
    }

}

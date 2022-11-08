package ru.practicum.ewm.publicAPI.service;

import com.google.gson.Gson;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.common.dto.EndpointHit;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventMapper;
import ru.practicum.ewm.common.dto.Stats;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final HitClient hitClient;

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
                getConfirmedRequest(event.getId()),
                getEventViews(event));
    }

    @Override
    public List<EventFullDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                      String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                      HttpServletRequest request) {

        Sort sort1 = Sort.unsorted();
        if (sort.equals("EVENT_DATE")) {
            sort1 = Sort.by("eventDate");
        }
        if (sort.equals("VIEWS")) {
            sort1 = Sort.by("views");
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
        Page<Event> result = eventRepository.findAll(expression, pageable);

        if (onlyAvailable) {
            result.stream()
                    .filter(event1 -> event1.getParticipantLimit() > getConfirmedRequest(event1.getId()))
                    .collect(Collectors.toList());
        }

        result.stream().peek(event1 -> hitClient.save(EndpointHit.builder().app("ewm-service")
                .timestamp(DateTimeMapper.toString(LocalDateTime.now()))
                .uri(request.getRequestURI() + "/" + event1.getId())
                .ip(request.getRemoteAddr()).build()));

        return result.stream()
                .map(event1 -> EventMapper.toEventFullDto(event1,
                        getConfirmedRequest(event1.getId()),
                        getEventViews(event1)))
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

}

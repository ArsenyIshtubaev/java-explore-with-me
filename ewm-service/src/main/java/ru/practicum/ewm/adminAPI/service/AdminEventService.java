package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.UpdateEventRequest;
import ru.practicum.ewm.common.enums.State;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> findAll(List<Long> users, List<State> states, List<Long> categories, String rangeStart,
                               String rangeEnd, int from, int size);

    EventFullDto update(long eventId, UpdateEventRequest updateEventRequest);

    EventFullDto publicationEvent(long eventId);

    EventFullDto rejectEvent(long eventId);


}

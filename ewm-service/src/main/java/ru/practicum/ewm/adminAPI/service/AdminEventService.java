package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.privateAPI.dto.EventFullDto;
import ru.practicum.ewm.privateAPI.dto.UpdateEventRequest;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> findAll(int [] users, String [] states, int [] categories, String rangeStart,
                               String rangeEnd, int from, int size);

    EventFullDto update(long eventId, UpdateEventRequest updateEventRequest);

    EventFullDto publicationEvent(long eventId);

    EventFullDto rejectEvent(long eventId);


}

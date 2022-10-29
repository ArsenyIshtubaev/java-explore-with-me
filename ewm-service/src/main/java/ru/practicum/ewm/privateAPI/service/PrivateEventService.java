package ru.practicum.ewm.privateAPI.service;

import ru.practicum.ewm.common.dto.*;

import java.util.List;

public interface PrivateEventService {

    EventFullDto findById(long userId, long eventId);

    List<EventShortDto> findAll(long userId, int from, int size);

    EventFullDto save(long userId, NewEventDto newEventDto);

    EventFullDto update(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto cancelEvent(long userId, long eventId);

    List<ParticipationRequestDto> findAllRequestEvents(long userId, long eventId);

    ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId);

    ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId);
}

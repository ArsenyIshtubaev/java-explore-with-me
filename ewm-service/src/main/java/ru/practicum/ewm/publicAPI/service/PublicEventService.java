package ru.practicum.ewm.publicAPI.service;

import ru.practicum.ewm.publicAPI.dto.EventFullDto;

import java.util.List;

public interface PublicEventService {

    EventFullDto findById(long id);

    List<EventFullDto> findAll(String text, long[] categories, Boolean paid,
                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                               String sort, int from, int size);

}

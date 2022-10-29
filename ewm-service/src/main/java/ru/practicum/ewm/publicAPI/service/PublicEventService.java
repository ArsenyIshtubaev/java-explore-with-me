package ru.practicum.ewm.publicAPI.service;

import ru.practicum.ewm.common.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {

    EventFullDto findById(long id, HttpServletRequest request);

    List<EventFullDto> findAll(String text, Long[] categories, Boolean paid, String rangeStart,
                               String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                               HttpServletRequest request);

}

package ru.practicum.ewm.adminAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.privateAPI.dto.EventFullDto;
import ru.practicum.ewm.privateAPI.dto.EventMapper;
import ru.practicum.ewm.privateAPI.dto.UpdateEventRequest;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService{

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public AdminEventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventFullDto> findAll(int[] users, String[] states, int[] categories, String rangeStart,
                                      String rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto update(long eventId, UpdateEventRequest updateEventRequest) {
        return null;
    }

    @Override
    public EventFullDto publicationEvent(long eventId) {
        return null;
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        return null;
    }
}

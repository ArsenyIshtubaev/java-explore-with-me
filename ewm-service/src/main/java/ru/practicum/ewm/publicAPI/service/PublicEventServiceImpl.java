package ru.practicum.ewm.publicAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.StorageException;
import ru.practicum.ewm.publicAPI.dto.EventFullDto;
import ru.practicum.ewm.publicAPI.dto.PublicEventMapper;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final PublicEventMapper eventMapper;

    @Autowired
    public PublicEventServiceImpl(EventRepository eventRepository, PublicEventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }


    @Override
    public EventFullDto findById(long id) {
        return eventMapper.toEventFullDto(eventRepository.findById(id)
                .orElseThrow(() -> new StorageException("Категории с Id = " + id + " нет в БД")));
    }

    @Override
    public List<EventFullDto> findAll(String text, long[] categories, Boolean paid, String rangeStart,
                                      String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {

        //
        return null;
    }
}

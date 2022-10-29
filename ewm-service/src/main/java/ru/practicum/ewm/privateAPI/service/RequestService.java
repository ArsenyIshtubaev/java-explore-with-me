package ru.practicum.ewm.privateAPI.service;

import ru.practicum.ewm.common.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> findAll(long userId);

    ParticipationRequestDto save(long userId, long eventId);

    ParticipationRequestDto update(long userId, long requestId);

}

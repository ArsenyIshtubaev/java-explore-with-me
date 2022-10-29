package ru.practicum.ewm.common.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.utills.DateTimeMapper;

@Component
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString(),
                DateTimeMapper.toString(request.getCreated())
        );
    }
}

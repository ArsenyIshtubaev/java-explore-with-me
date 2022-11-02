package ru.practicum.ewm.common.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.utills.DateTimeMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString(),
                DateTimeMapper.toString(request.getCreated())
        );
    }
}

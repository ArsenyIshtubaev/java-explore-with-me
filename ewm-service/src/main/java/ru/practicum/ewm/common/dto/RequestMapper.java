package ru.practicum.ewm.common.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.utills.DateTimeMapper;

@Component
@NoArgsConstructor
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

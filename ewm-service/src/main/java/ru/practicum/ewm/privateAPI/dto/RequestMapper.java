package ru.practicum.ewm.privateAPI.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddТHH:mm:ss.SSS");

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString(),
                request.getCreated().format(formatter)
        );
    }

    public Request toRequest(ParticipationRequestDto requestDto, Event event, User requester) {
        return new Request(requestDto.getId(),
                event,
                requester,
                State.valueOf(requestDto.getStatus()),
                LocalDateTime.parse(requestDto.getCreated(), formatter)
        );
    }
}

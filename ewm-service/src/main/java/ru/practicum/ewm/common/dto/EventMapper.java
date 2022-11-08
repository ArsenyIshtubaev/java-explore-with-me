package ru.practicum.ewm.common.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.model.Category;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        return new Event(null,
                newEventDto.getAnnotation(),
                category,
                newEventDto.getDescription(),
                DateTimeMapper.toDateTime(newEventDto.getEventDate()),
                LocalDateTime.now(),
                null,
                newEventDto.getLocation(),
                initiator,
                newEventDto.getPaid(),
                newEventDto.getTitle(),
                newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration(),
                State.PENDING);
    }

    public static EventFullDto toEventFullDto(Event event, Integer confirmedRequest, Integer views) {
        String published = null;
        if (event.getPublishedOn() != null) {
            published = DateTimeMapper.toString(event.getPublishedOn());
        }
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequest,
                DateTimeMapper.toString(event.getCreatedOn()),
                event.getDescription(),
                DateTimeMapper.toString(event.getEventDate()),
                UserMapper.toUserDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                published,
                event.getRequestModeration(),
                event.getState().name(),
                event.getTitle(),
                views);
    }

    public static EventShortDto toEventShortDto(Event event, Integer confirmedRequest, Integer views) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                DateTimeMapper.toString(event.getEventDate()),
                event.getPaid(),
                event.getTitle(),
                confirmedRequest,
                UserMapper.toUserDto(event.getInitiator()),
                views);
    }
}

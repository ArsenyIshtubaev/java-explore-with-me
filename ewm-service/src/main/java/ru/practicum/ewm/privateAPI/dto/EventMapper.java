package ru.practicum.ewm.privateAPI.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.adminAPI.dto.CategoryMapper;
import ru.practicum.ewm.adminAPI.dto.UserDto;
import ru.practicum.ewm.adminAPI.dto.UserMapper;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.utills.DateTimeMapper;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    private final CategoryRepository categoryRepository;

    public EventMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public NewEventDto toNewEventDto(Event event) {
        return new NewEventDto(event.getAnnotation(),
                event.getCategory().getId(),
                event.getDescription(),
                event.getEventDate(),
                event.getLocation(),
                event.getPaid(),
                event.getTitle(),
                event.getParticipantLimit(),
                event.getRequestModeration());
    }

    public Event toEvent(NewEventDto newEventDto, User initiator) {
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        return new Event(null,
                newEventDto.getAnnotation(),
                categoryRepository.findById(newEventDto.getCategory()).orElse(null),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                LocalDateTime.now(),
                null,
                newEventDto.getLocation(),
                initiator,
                newEventDto.getPaid(),
                newEventDto.getTitle(),
                newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration(),
                State.PENDING,
                0L);
    }

    public EventFullDto toEventFullDto(Event event, Integer confirmedRequest, UserDto initiator) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                confirmedRequest,
                DateTimeMapper.toString(event.getCreatedOn()),
                event.getDescription(),
                DateTimeMapper.toString(event.getEventDate()),
                initiator,
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                DateTimeMapper.toString(event.getPublishedOn()),
                event.getRequestModeration(),
                event.getState().name(),
                event.getTitle(),
                event.getViews());
    }

    public EventShortDto toEventShortDto(Event event, Integer confirmedRequest,
                                         UserMapper userMapper, CategoryMapper categoryMapper) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                DateTimeMapper.toString(event.getEventDate()),
                event.getPaid(),
                event.getTitle(),
                confirmedRequest,
                userMapper.toUserDto(event.getInitiator()),
                event.getViews());
    }
}

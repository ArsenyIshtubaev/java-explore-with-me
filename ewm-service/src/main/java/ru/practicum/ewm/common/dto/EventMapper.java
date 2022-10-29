package ru.practicum.ewm.common.dto;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.CategoryRepository;
import ru.practicum.ewm.common.utills.DateTimeMapper;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final HitClient hitClient;

    public EventMapper(CategoryRepository categoryRepository, CategoryMapper categoryMapper, HitClient hitClient) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.hitClient = hitClient;
    }

    public Event toEvent(NewEventDto newEventDto, User initiator) {
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        return new Event(null,
                newEventDto.getAnnotation(),
                categoryRepository.findById(newEventDto.getCategory()).orElse(null),
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

    public EventFullDto toEventFullDto(Event event, Integer confirmedRequest, UserDto initiator) {
        Gson gson = new Gson();
        Integer views = 0;
        String published = null;
        if (event.getPublishedOn() != null) {
            published = DateTimeMapper.toString(event.getPublishedOn());
        }
        String[] uris = new String[]{"http://ewm-service:8080/events/" + event.getId()};
        ResponseEntity<Object> objectResponseEntity = hitClient
                .getStats(uris,
                        DateTimeMapper.toString(event.getCreatedOn()),
                        DateTimeMapper.toString(LocalDateTime.now()), false);
        if (objectResponseEntity.getStatusCode() == HttpStatus.OK) {
            String responseJson = gson.toJson(objectResponseEntity.getBody());
            Stats stats = gson.fromJson(responseJson, Stats.class);
            views = stats.getStats().get(0).getHits();
        }
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequest,
                DateTimeMapper.toString(event.getCreatedOn()),
                event.getDescription(),
                DateTimeMapper.toString(event.getEventDate()),
                initiator,
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                published,
                event.getRequestModeration(),
                event.getState().name(),
                event.getTitle(),
                views);
    }

    public EventShortDto toEventShortDto(Event event, Integer confirmedRequest,
                                         UserMapper userMapper) {
        Gson gson = new Gson();
        Integer views = 0;
        String[] uris = new String[]{"http://ewm-service:8080/events/" + event.getId()};
        ResponseEntity<Object> objectResponseEntity = hitClient
                .getStats(uris,
                        DateTimeMapper.toString(event.getCreatedOn()),
                        DateTimeMapper.toString(LocalDateTime.now()), false);
        if (objectResponseEntity.getStatusCode() == HttpStatus.OK) {
            String responseJson = gson.toJson(objectResponseEntity.getBody());
            Stats stats = gson.fromJson(responseJson, Stats.class);
            views = stats.getStats().get(0).getHits();
        }
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                DateTimeMapper.toString(event.getEventDate()),
                event.getPaid(),
                event.getTitle(),
                confirmedRequest,
                userMapper.toUserDto(event.getInitiator()),
                views);
    }
}

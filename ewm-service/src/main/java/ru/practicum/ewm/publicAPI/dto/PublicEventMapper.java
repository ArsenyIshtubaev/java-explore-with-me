package ru.practicum.ewm.publicAPI.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.adminAPI.dto.UserMapper;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;

@Component
public class PublicEventMapper {

    private final UserMapper userMapper;
    private final HitClient hitClient;


    @Autowired
    public PublicEventMapper(UserMapper userMapper, HitClient hitClient) {
        this.userMapper = userMapper;
        this.hitClient = hitClient;
    }

    public EventFullDto toEventFullDto(Event event) {
        ViewStats viewStats = (ViewStats) hitClient.getStats(("http://localhost:8080/events/" + event.getId()),
                event.getCreatedOn(), LocalDateTime.now(), false).getBody();
        Integer views = 0;
        if (viewStats != null) {
            views = viewStats.getHits();
        }
        return new EventFullDto(event.getId(), event.getAnnotation(),
                event.getCategory(), event.getDescription(), event.getEventDate(),
                event.getCreatedOn(), event.getLocation(), userMapper.toUserDto(event.getInitiator()),
                event.getPaid(), event.getTitle(), event.getParticipantLimit(),
                event.getRequestModeration(), event.getState(), views);
    }

}

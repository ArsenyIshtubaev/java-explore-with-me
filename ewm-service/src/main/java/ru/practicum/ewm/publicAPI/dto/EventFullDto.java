package ru.practicum.ewm.publicAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.adminAPI.dto.UserDto;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;
    private String annotation;
    private Category category;
    private String description;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;
    private Location location;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Integer participantLimit;
    private Boolean requestModeration;
    private State state;
    private Integer views;

}

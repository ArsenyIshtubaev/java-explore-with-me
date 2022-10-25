package ru.practicum.ewm.privateAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.adminAPI.dto.UserDto;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;
    @NotNull
    private String annotation;
    private Integer confirmedRequests;
    private String createdOn; //создание
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private UserDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn; //публикация
    private Boolean requestModeration;
    private String state;
    @NotNull
    private String title;
    private Long views;

}

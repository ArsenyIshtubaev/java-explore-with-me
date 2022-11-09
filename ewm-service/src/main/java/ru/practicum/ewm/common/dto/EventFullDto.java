package ru.practicum.ewm.common.dto;

import lombok.*;
import ru.practicum.ewm.common.model.Location;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;
    @NotNull
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
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
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    @NotNull
    private String title;
    private Integer views;

}

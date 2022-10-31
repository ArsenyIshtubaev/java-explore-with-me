package ru.practicum.ewm.common.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    private String eventDate;
    @NotNull
    private Boolean paid;
    @NotNull
    private String title;
    private Integer confirmedRequests;
    private UserDto initiator;
    private Integer views;

}

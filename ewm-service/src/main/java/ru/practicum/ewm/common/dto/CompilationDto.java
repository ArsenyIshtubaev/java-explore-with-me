package ru.practicum.ewm.common.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    @Positive
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotNull
    private String title;
    private Set<EventShortDto> events;

}

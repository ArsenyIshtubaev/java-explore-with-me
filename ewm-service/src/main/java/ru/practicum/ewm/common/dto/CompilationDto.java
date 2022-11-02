package ru.practicum.ewm.common.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
    private Set<EventShortDto> events;

}

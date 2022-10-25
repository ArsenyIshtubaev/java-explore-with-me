package ru.practicum.ewm.adminAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.privateAPI.dto.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotNull
    private String title;
    private Set<EventShortDto> events;

}

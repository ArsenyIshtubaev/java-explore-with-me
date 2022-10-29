package ru.practicum.ewm.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Long id;
    private Boolean pinned;
    @NotNull
    private String title;
    private Set<Long> events;

}

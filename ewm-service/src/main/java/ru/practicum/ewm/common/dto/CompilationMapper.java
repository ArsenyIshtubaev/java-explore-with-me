package ru.practicum.ewm.common.dto;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.model.Event;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        return new Compilation(null,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle(),
                events
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, Set<EventShortDto> events) {

        return new CompilationDto(compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                events);
    }
}

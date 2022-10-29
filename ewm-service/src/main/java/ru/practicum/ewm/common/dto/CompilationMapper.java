package ru.practicum.ewm.common.dto;


import org.springframework.stereotype.Component;
import ru.practicum.ewm.common.model.Compilation;
import ru.practicum.ewm.common.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        return new Compilation(null,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle(),
                events
        );
    }

    public NewCompilationDto toNewCompilation(Compilation compilation) {
        Set<Long> events = compilation.getEvents()
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        return new NewCompilationDto(compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                events);
    }

    public CompilationDto toCompilationDto(Compilation compilation, Set<EventShortDto> events) {

        return new CompilationDto(compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                events);
    }
}

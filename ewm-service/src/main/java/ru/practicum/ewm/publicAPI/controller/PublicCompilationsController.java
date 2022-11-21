package ru.practicum.ewm.publicAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.publicAPI.service.PublicCompilationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationsController {

    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> findAll(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get all compilations");
        return publicCompilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDto findById(@PathVariable long id) {
        log.info("Get compilation id={}", id);
        return publicCompilationService.findById(id);
    }
}

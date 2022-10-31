package ru.practicum.ewm.adminAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.service.AdminCompilationService;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    public CompilationDto create(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST : '{}', compilation title={}", "/admin/compilations",
                newCompilationDto.getTitle());
        return adminCompilationService.save(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable long compId) {
        log.info("Delete compilation id={}", compId);
        adminCompilationService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventById(@PathVariable long compId, @PathVariable long eventId) {
        log.info("Delete event from compilation compId={}, eventId={}", compId, eventId);
        adminCompilationService.deleteEventById(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("PATCH compilation id={}, eventId={}", compId, eventId);
        adminCompilationService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpin(@PathVariable long compId) {
        log.info("Delete compilation from pin compId={}", compId);
        adminCompilationService.unpin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pin(@PathVariable long compId) {
        log.info("PATCH compilation add pin compId={}", compId);
        adminCompilationService.pin(compId);
    }
}

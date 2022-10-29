package ru.practicum.ewm.adminAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.CompilationDto;
import ru.practicum.ewm.common.dto.NewCompilationDto;
import ru.practicum.ewm.adminAPI.service.AdminCompilationService;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;

    @Autowired
    public AdminCompilationsController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

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
    public void updateCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("PATCH compilation id={}, eventId={}", compId, eventId);
        adminCompilationService.updateCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deletePinById(@PathVariable long compId) {
        log.info("Delete compilation from pin compId={}", compId);
        adminCompilationService.deletePinById(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void updatePin(@PathVariable long compId) {
        log.info("PATCH compilation add pin compId={}", compId);
        adminCompilationService.updatePin(compId);
    }
}

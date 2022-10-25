package ru.practicum.ewm.adminAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.service.AdminEventService;
import ru.practicum.ewm.privateAPI.dto.EventFullDto;
import ru.practicum.ewm.privateAPI.dto.UpdateEventRequest;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @Autowired
    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    @GetMapping()
    public List<EventFullDto> findAll(@RequestParam(required = false) int[] users,
                                      @RequestParam(required = false) String[] states,
                                      @RequestParam(required = false) int[] categories,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Get events");
        return adminEventService.findAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(@PathVariable long eventId,
                               @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Update event id={}", eventId);
        return adminEventService.update(eventId, updateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publicationEvent(@PathVariable long eventId) {

        log.info("Publish event id={}", eventId);
        return adminEventService.publicationEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.info("Reject event id={}", eventId);
        return adminEventService.rejectEvent(eventId);
    }

}

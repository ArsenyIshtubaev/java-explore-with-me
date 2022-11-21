package ru.practicum.ewm.privateAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.privateAPI.service.PrivateEventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @GetMapping("/{eventId}")
    public EventFullDto findCategoryById(@PathVariable long userId,
                                         @PathVariable long eventId) {
        log.info("Get event id={}", eventId);
        return privateEventService.findById(userId, eventId);
    }

    @PostMapping
    public EventFullDto create(@PathVariable long userId,
                               @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST : '{}', event annotation={}", "/users/{userId}/events",
                newEventDto.getAnnotation());
        return privateEventService.save(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> findAll(@PathVariable long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return privateEventService.findAll(userId, from, size);
    }

    @PatchMapping
    public EventFullDto update(@PathVariable long userId,
                               @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("PATCH event id={}, annotation={}", updateEventRequest.getEventId(),
                updateEventRequest.getAnnotation());
        return privateEventService.update(userId, updateEventRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId,
                                    @PathVariable long eventId) {
        log.info("Cancel event id={}", eventId);
        return privateEventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findAllRequestEvents(@PathVariable long userId,
                                                              @PathVariable long eventId) {
        return privateEventService.findAllRequestEvents(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable long userId,
                                                  @PathVariable long eventId, @PathVariable long reqId) {
        log.info("Confirm request eventId={}, reqId={}", eventId, reqId);
        return privateEventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                 @PathVariable long eventId, @PathVariable long reqId) {
        log.info("Reject request eventId={}, reqId={}", eventId, reqId);
        return privateEventService.rejectRequest(userId, eventId, reqId);
    }

}

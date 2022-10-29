package ru.practicum.ewm.privateAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.ParticipationRequestDto;
import ru.practicum.ewm.privateAPI.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable long userId) {
        log.info("Get users requests id={}", userId);
        return requestService.findAll(userId);
    }

    @PostMapping
    public ParticipationRequestDto create(@PathVariable long userId,
                                          @RequestParam(required = true) long eventId) {
        log.info("Post users requests userId={}, evenId={}", userId, eventId);
        return requestService.save(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto update(@PathVariable long userId, @PathVariable long requestId) {
        log.info("PATCH userId={}, requestId={}", userId, requestId);
        return requestService.update(userId, requestId);
    }

}

package ru.practicum.ewm.privateAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.ParticipationRequestDto;
import ru.practicum.ewm.privateAPI.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable long userId) {
        log.info("Get users requests id={}", userId);
        return requestService.findAll(userId);
    }

    @PostMapping
    public ParticipationRequestDto create(@PathVariable long userId,
                                          @RequestParam long eventId) {
        log.info("Post users requests userId={}, evenId={}", userId, eventId);
        return requestService.save(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.info("PATCH userId={}, requestId={}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

}

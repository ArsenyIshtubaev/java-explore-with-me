package ru.practicum.ewm.publicAPI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.publicAPI.dto.EventFullDto;
import ru.practicum.ewm.publicAPI.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;
    private final HitClient hitClient;

    @Autowired
    public PublicEventController(PublicEventService publicEventService, HitClient hitClient) {
        this.publicEventService = publicEventService;
        this.hitClient = hitClient;
    }

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) long[] categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        hitClient.save(Hit.builder().app("ewm-service")
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr()).build());

        return publicEventService.findAll(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable long id, HttpServletRequest request) {

        hitClient.save(Hit.builder().app("ewm-service")
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr()).build());

        return publicEventService.findById(id);
    }

}

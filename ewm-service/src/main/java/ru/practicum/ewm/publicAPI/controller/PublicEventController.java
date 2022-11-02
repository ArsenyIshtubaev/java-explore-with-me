package ru.practicum.ewm.publicAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.publicAPI.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Get all events");
        return publicEventService.findAll(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable long id,
                                 HttpServletRequest request) {
        log.info("Get event id={}", id);
        return publicEventService.findById(id, request);
    }

}

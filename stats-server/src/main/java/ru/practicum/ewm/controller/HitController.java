package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.HitService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class HitController {

    private final HitService hitService;

    @GetMapping("/stats")
    public List<ViewStats> findStats(@RequestParam(required = true) List<String> uris,
                                     @RequestParam(required = true) String start,
                                     @RequestParam(required = true) String end,
                                     @RequestParam(defaultValue = "false") Boolean unique) {
        return hitService.findStats(uris, start, end, unique);
    }

    @PostMapping("/hit")
    public EndpointHit create(@RequestBody EndpointHit endpointHit) {
        log.info("Получен запрос к эндпоинту: '{} {}', App: {} и Uri: {}", "POST", "/hit",
                endpointHit.getApp(), endpointHit.getUri());
        return hitService.save(endpointHit);
    }
}

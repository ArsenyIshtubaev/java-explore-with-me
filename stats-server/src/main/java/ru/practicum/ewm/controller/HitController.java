package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class HitController {

    private final HitService hitService;

    @Autowired
    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @GetMapping("/stats")
    public List<ViewStats> findStats(@RequestParam(required = true) String[] uris,
                                     @RequestParam(required = true) LocalDateTime start,
                                     @RequestParam(required = true) LocalDateTime end,
                                     @RequestParam(defaultValue = "false") Boolean unique) {
        return hitService.findStats(uris, start, end, unique);
    }

    @PostMapping("/hit")
    public Hit create(@RequestBody Hit hit) {
        log.info("Получен запрос к эндпоинту: '{} {}', App: {} и Uri: {}", "POST", "/hit",
                hit.getApp(), hit.getUri());
        return hitService.save(hit);
    }
}

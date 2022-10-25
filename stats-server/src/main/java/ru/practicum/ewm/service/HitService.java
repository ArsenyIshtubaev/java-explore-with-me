package ru.practicum.ewm.service;

import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    Hit save(Hit hit);

    List<ViewStats> findStats(String[] uris, LocalDateTime start, LocalDateTime end, Boolean unique);

}

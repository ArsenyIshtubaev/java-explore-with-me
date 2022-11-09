package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.Stats;
import ru.practicum.ewm.dto.ViewStats;

import java.util.List;
import java.util.Set;

public interface HitService {

    EndpointHit save(EndpointHit endpointHit);

    Set<ViewStats> findStats(List<String> uris, String start, String end, Boolean unique);

    Stats findViews(List<String> uris, String start, String end, Boolean unique);
}

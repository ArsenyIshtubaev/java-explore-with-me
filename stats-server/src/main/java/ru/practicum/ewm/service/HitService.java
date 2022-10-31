package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;

import java.util.List;

public interface HitService {

    EndpointHit save(EndpointHit endpointHit);

    List<ViewStats> findStats(List<String> uris, String start, String end, Boolean unique);

}

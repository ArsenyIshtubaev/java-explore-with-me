package ru.practicum.ewm.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.utills.DateTimeMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {

    public static EndpointHit toEndpointHi(Hit hit) {
        return new EndpointHit(hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                DateTimeMapper.toString(hit.getTimestamp()));
    }

    public static Hit toHit(EndpointHit endpointHit) {
        return new Hit(endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                DateTimeMapper.toDateTime(endpointHit.getTimestamp())
        );
    }

}

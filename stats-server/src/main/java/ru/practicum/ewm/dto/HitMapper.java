package ru.practicum.ewm.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.utills.DateTimeMapper;

@Component
public class HitMapper {

    public EndpointHit toEndpointHi(Hit hit) {
        return new EndpointHit(hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                DateTimeMapper.toString(hit.getTimestamp()));
    }

    public Hit toHit(EndpointHit endpointHit) {
        return new Hit(endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                DateTimeMapper.toDateTime(endpointHit.getTimestamp())
        );
    }

}

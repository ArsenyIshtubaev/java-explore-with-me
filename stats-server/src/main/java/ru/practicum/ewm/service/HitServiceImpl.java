package ru.practicum.ewm.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.HitMapper;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.QHit;
import ru.practicum.ewm.repository.HitRepository;
import ru.practicum.ewm.utills.DateTimeMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    @Override
    public EndpointHit save(EndpointHit endpointHit) {
        Hit hit = hitMapper.toHit(endpointHit);
        return hitMapper.toEndpointHi(hitRepository.save(hit));
    }

    @Override
    public List<ViewStats> findStats(List<String> uris, String start, String end, Boolean unique) {

        List<ViewStats> stats = new ArrayList<>();

        QHit hit = QHit.hit;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (uris != null && !uris.isEmpty()) {
            conditions.add(hit.uri.in(uris));
        }
        if (start != null && end != null) {
            conditions.add(hit.timestamp.between(DateTimeMapper.toDateTime(start), DateTimeMapper.toDateTime(end)));
        }

        BooleanExpression expression = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Hit> hitList = hitRepository.findAll(expression, Pageable.unpaged()).toList();

        for (Hit h : hitList) {
            Integer hits;
            if (unique) {
                hits = hitRepository.findHitsWithUniqueIp(h.getUri(), h.getApp());
            } else {
                hits = hitRepository.findHits(h.getUri(), h.getApp());
            }
            stats.add(new ViewStats(h.getApp(), h.getUri(), hits));
        }
        return stats;

    }

}

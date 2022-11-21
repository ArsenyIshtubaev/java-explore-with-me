package ru.practicum.ewm.common.utills;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.common.dto.ConfirmedRequestDto;
import ru.practicum.ewm.common.dto.Stats;
import ru.practicum.ewm.common.dto.ViewStats;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchEventValues {

    public static final String URI = "/events/";

    private final RequestRepository requestRepository;
    private final HitClient hitClient;

    public Long getConfirmedRequest(long eventId) {
        long confirmedRequest = 0L;
        List<Request> requests = requestRepository.findAllByEventIdAndStatus(eventId, State.CONFIRMED);
        if (!requests.isEmpty()) {
            confirmedRequest = (long) requests.size();
        }
        return confirmedRequest;
    }

    public Integer getEventViews(Event event) {
        Gson gson = new Gson();
        Integer views = 0;
        List<String> uris = new ArrayList<>();
        uris.add(URI + event.getId());
        ResponseEntity<Object> objectResponseEntity = hitClient
                .getStats(uris,
                        "2020-05-05 00:00:00",
                        DateTimeMapper.toString(LocalDateTime.now()),
                        false);
        if (objectResponseEntity.getStatusCode() == HttpStatus.OK) {
            String responseJson = gson.toJson(objectResponseEntity.getBody());
            Stats stats = gson.fromJson(responseJson, Stats.class);
            Set<ViewStats> viewStatsSet = stats.getStats();
            if (!viewStatsSet.isEmpty()) {
                views = viewStatsSet.stream().findAny().orElseThrow().getHits();
            }
        }
        return views;
    }

    public HashMap<Long, Long> getConfirmedRequests(List<Long> ids) {
        HashMap<Long, Long> confirmedRequests = new HashMap<>();
        List<ConfirmedRequestDto> confirmedRequestDtos = requestRepository.findConfirmedRequests(ids, State.CONFIRMED);
        for (ConfirmedRequestDto confirmedRequestDto : confirmedRequestDtos) {
            confirmedRequests.put(confirmedRequestDto.getEventId(), confirmedRequestDto.getConfirmedRequests());
        }
        return confirmedRequests;
    }

    public HashMap<Long, Integer> getEventsViews(List<Long> ids) {
        Gson gson = new Gson();
        HashMap<Long, Integer> eventViews = new HashMap<>();
        List<String> uris = new ArrayList<>();
        for (Long id : ids) {
            uris.add(URI + id);
            eventViews.put(id, 0);
        }
        ResponseEntity<Object> objectResponseEntity = hitClient
                .getStats(uris,
                        "2020-05-05 00:00:00",
                        DateTimeMapper.toString(LocalDateTime.now()),
                        false);
        if (objectResponseEntity.getStatusCode() == HttpStatus.OK) {
            String responseJson = gson.toJson(objectResponseEntity.getBody());
            Stats stats = gson.fromJson(responseJson, Stats.class);
            if (!stats.getStats().isEmpty()) {
                for (Long id : ids) {
                    String uri = URI + id;
                    for (ViewStats viewStats : stats.getStats()) {
                        if (viewStats.getUri().equals(uri)) {
                            eventViews.put(id, viewStats.getHits());
                        }
                    }
                }
            }
        }
        return eventViews;
    }
}

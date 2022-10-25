package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class HitClient extends BaseClient {

    private static final String API_PREFIX = "";

    @Autowired
    public HitClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> save(Hit hit) {
        return post("/hit", hit);
    }

    public ResponseEntity<Object> getStats(String uri, LocalDateTime start, LocalDateTime end, Boolean unique) {
        if (unique == null) {
            unique = false;
        }
        Map<String, Object> parameters = Map.of(
                "uri", uri,
                "start", start,
                "end", end,
                "unique", unique
        );
        return get("/stats?uri={uri}&start={start}&end={end}&unique={unique}", parameters);
    }

}

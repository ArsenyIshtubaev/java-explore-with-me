package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.common.dto.EndpointHit;
import ru.practicum.ewm.common.dto.ViewStats;

import java.util.List;
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

    public ResponseEntity<Object> save(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public ResponseEntity<Object> getStats(String[] uris, String start, String end, Boolean unique) {

        StringBuilder uriString = new StringBuilder();
        for (String uri : uris){
            uriString.append("uris=").append(uri).append("&");
        }
        Map<String, Object> parameters = Map.of(
                "uris", uriString.toString(),
                "start", start,
                "end", end,
                "unique", unique
        );
        return get("/stats?{uris}start={start}&end={end}&unique={unique}", parameters);
    }

}

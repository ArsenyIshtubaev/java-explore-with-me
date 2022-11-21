package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.common.dto.EndpointHit;

import java.util.List;
import java.util.Map;

@Service
public class HitClient extends BaseClient {

    @Autowired
    public HitClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        //stats-server:9090
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> save(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public ResponseEntity<Object> getStats(List<String> uris, String start, String end, boolean unique) {

        StringBuilder uriString = new StringBuilder();
        uriString.append(uris.get(0));
        if (uris.size() > 1) {
            for (int i = 1; i < uris.size(); i++) {
                uriString.append("&").append(uris.get(i));
            }
        }
        Map<String, Object> parameters = Map.of(
                "uris", uriString.toString(),
                "start", start,
                "end", end,
                "unique", unique
        );
        return get("/views?uris={uris}&start={start}&end={end}&unique={unique}", parameters);
    }

}

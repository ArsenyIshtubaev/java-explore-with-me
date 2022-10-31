package ru.practicum.ewm.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHit {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

}

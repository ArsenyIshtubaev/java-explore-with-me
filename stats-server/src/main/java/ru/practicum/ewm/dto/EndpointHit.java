package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utills.IpAddress;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {

    private Long id;
    private String app;
    private String uri;
    @IpAddress
    private String ip;
    private String timestamp;

}

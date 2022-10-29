package ru.practicum.ewm.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private Error[] errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

}

package ru.practicum.ewm.common.exception;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private List<Error> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

}

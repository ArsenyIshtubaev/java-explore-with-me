package ru.practicum.ewm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    @NotBlank(message = "comment should not be blank")
    private String text;
    private Long eventId;
    private UserDto commentator;
    private LocalDateTime written;

}

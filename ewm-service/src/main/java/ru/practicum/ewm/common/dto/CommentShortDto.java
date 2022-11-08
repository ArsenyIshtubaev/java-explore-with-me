package ru.practicum.ewm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {

    private Long id;
    @NotBlank(message = "comment should not be blank")
    private String text;

}

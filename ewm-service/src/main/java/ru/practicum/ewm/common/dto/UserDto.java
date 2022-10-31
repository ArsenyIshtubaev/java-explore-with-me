package ru.practicum.ewm.common.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @Email(message = "incorrect email")
    @NotBlank(message = "email should not be blank")
    private String email;
    @NotBlank(message = "name should not be blank")
    private String name;
}

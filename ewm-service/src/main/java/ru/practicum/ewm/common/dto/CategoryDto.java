package ru.practicum.ewm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private long id;
    @NotBlank(message = "name should not be blank")
    private String name;

}

package ru.practicum.ewm.common.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    private List<ViewStats> stats;
}

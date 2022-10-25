package ru.practicum.ewm.model;

import org.springframework.stereotype.Component;

@Component
public class HitMapper {

    public ViewStats toViewStats(Hit hit) {
        return new ViewStats(hit.getApp(), hit.getUri(), null);
    }
}

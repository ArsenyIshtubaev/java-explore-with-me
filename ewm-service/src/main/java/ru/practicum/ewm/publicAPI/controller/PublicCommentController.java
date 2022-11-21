package ru.practicum.ewm.publicAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.publicAPI.service.PublicCommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {

    private final PublicCommentService publicCommentService;

    @GetMapping("/{eventId}")
    public List<CommentDto> findAllByEventId(@PathVariable long eventId) {
        log.info("Get all comments be event with Id = {}", eventId);
        return publicCommentService.findAllByEventId(eventId);
    }

}

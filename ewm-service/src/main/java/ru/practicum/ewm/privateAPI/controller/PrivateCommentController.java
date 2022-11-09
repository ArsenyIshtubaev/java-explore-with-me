package ru.practicum.ewm.privateAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.CommentShortDto;
import ru.practicum.ewm.privateAPI.service.PrivateCommentService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    @PostMapping("/{eventId}")
    public CommentDto create(@PathVariable long userId,
                             @PathVariable long eventId,
                             @RequestBody @Valid CommentShortDto commentShortDto) {
        log.info("Post request: '{} {}', Comment: Text: {}", "POST", "/admin/comments",
                commentShortDto.getText());
        return privateCommentService.publicationComment(userId, eventId, commentShortDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteById(@PathVariable long userId,
                           @PathVariable long commentId) {
        log.info("Delete comment id={}", commentId);
        privateCommentService.delete(userId, commentId);
    }

    @PatchMapping
    public CommentDto update(@PathVariable long userId,
                             @RequestBody @Valid CommentDto commentDto) {
        log.info("PATCH comment id={}, text={}", commentDto.getId(), commentDto.getText());
        return privateCommentService.update(userId, commentDto);
    }

}

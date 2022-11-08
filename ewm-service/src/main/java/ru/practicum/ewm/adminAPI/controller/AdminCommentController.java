package ru.practicum.ewm.adminAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminAPI.service.AdminCommentService;
import ru.practicum.ewm.common.dto.CommentDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @GetMapping("/{commentId}")
    public CommentDto findById(@PathVariable long commentId) {
        log.info("GET comment id={}", commentId);
        return adminCommentService.findById(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteById(@PathVariable long commentId) {
        log.info("Delete comment id={}", commentId);
        adminCommentService.delete(commentId);
    }

}

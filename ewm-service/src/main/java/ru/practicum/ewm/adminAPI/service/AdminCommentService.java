package ru.practicum.ewm.adminAPI.service;

import ru.practicum.ewm.common.dto.CommentDto;

public interface AdminCommentService {

    CommentDto findById(long commentId);

    void delete(long commentId);

}

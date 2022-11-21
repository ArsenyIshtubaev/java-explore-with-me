package ru.practicum.ewm.privateAPI.service;

import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.CommentShortDto;

public interface PrivateCommentService {

    CommentDto update(long userId,
                      CommentDto commentDto);

    CommentDto publicationComment(long userId,
                                  long eventId,
                                  CommentShortDto commentShortDto);

    void delete(long userId,
                long commentId);
}

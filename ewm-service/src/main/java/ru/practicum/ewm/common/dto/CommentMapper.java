package ru.practicum.ewm.common.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.model.Comment;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {

    public static CommentDto toCommentDto(CommentShortDto commentShortDto,
                                          long eventId,
                                          User user) {
        return new CommentDto(
                null,
                commentShortDto.getText(),
                eventId,
                UserMapper.toUserDto(user),
                LocalDateTime.now()
        );
    }

    public static Comment toComment(CommentDto commentDto, Event event) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                event,
                UserMapper.toUser(commentDto.getCommentator()),
                commentDto.getWritten()
        );
    }


    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getEvent().getId(),
                UserMapper.toUserDto(comment.getCommentator()),
                comment.getWritten()
        );
    }
}

package ru.practicum.ewm.publicAPI.service;

import ru.practicum.ewm.common.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {

    List<CommentDto> findAllByEventId(long eventId);

}

package ru.practicum.ewm.privateAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.CommentMapper;
import ru.practicum.ewm.common.dto.CommentShortDto;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Comment;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.CommentRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto update(long userId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndCommentator_Id(commentDto.getId(), userId)
                .orElseThrow(() -> new StorageException("Comment with Id = " + commentDto.getId() + " not found"));
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto publicationComment(long userId, long eventId, CommentShortDto commentShortDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("User with Id = " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new StorageException("Event with Id = " + eventId + " not found"));
        CommentDto commentDto = CommentMapper.toCommentDto(commentShortDto, eventId, user);
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, event)));
    }

    @Override
    @Transactional
    public void delete(long userId, long commentId) {
        commentRepository.deleteById(commentId);
    }
}

package ru.practicum.ewm.adminAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.CommentMapper;
import ru.practicum.ewm.common.exception.StorageException;
import ru.practicum.ewm.common.model.Comment;
import ru.practicum.ewm.common.repository.CommentRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentDto findById(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new StorageException("Comment with Id = " + commentId + " not found"));
        return CommentMapper.toCommentDto(comment);
    }


    @Override
    @Transactional
    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }
}

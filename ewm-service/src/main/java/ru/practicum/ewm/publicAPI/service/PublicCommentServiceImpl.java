package ru.practicum.ewm.publicAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.CommentMapper;
import ru.practicum.ewm.common.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> findAllByEventId(long eventId) {
        return commentRepository.findAllByEvent_Id(eventId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

}

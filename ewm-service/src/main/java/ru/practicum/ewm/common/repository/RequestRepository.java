package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long userId);

    Optional<Request> findByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByEventIdAndStatus(long eventId, State status);

    Optional<Request> findByIdAndEventId(long reqId, long eventId);

}

package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.common.dto.ConfirmedRequestDto;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.model.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long userId);

    Optional<Request> findByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByEventIdAndStatus(long eventId, State status);

    Optional<Request> findByIdAndEventId(long reqId, long eventId);

    @Query("select new ru.practicum.ewm.common.dto.ConfirmedRequestDto(r.event.id, count(r)) from Request r " +
            "where r.event.id = ?1 and r.status = ?2 group by r.event.id ")
    List<ConfirmedRequestDto> findConfirmedRequests(List<Long> ids, State status);

}

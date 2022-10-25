package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(" select e from Event e " +
            "where upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            " or upper(e.description) like upper(concat('%', ?1, '%'))")
    List<Event> search(String text, Pageable pageable);

    List<Event> findByInitiatorId(long userId, Pageable pageable);

}

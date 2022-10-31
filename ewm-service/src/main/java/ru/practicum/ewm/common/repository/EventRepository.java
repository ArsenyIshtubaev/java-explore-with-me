package ru.practicum.ewm.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.common.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event>,
        QuerydslPredicateExecutor<Event> {

    @Query(" select e from Event e " +
            "where upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            " or upper(e.description) like upper(concat('%', ?1, '%'))")
    List<Event> search(String text, Pageable pageable);

    List<Event> findByInitiatorId(long userId, Pageable pageable);

}

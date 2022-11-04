package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long>, QuerydslPredicateExecutor<Hit> {

    @Query("select count(h.uri) as hits from Hit h " +
            "where h.uri = ?1 and h.app = ?2 group by h.app, h.uri ")
    Integer findHits(String uri, String app);

    @Query("select count(h.uri) as hits from Hit h " +
            "where h.ip = (select distinct h1.ip from Hit h1) and h.uri = ?1 and h.app = ?2 group by h.app, h.uri ")
    Integer findHitsWithUniqueIp(String uri, String app);


}

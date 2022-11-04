package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}

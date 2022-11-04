package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

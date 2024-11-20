package ru.gogolin.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogolin.task.entities.Priority;

import java.util.Optional;

public interface PrioritiesRepository extends JpaRepository<Priority, Integer> {
    Optional<Priority> findPriorityByName(String name);
}

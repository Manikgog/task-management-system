package ru.gogolin.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogolin.task.entities.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findById(long id);
}

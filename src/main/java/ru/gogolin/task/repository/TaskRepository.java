package ru.gogolin.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogolin.task.entity.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findById(long id);
}

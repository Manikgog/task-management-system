package ru.gogolin.task.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogolin.task.entities.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findById(long id);
    Page<Task> findByAuthor_Username(String username, Pageable pageable);
    Optional<Task> findByTitle(String title);
    Page<Task> findByExecutor_Username(String username, Pageable pageable);
}

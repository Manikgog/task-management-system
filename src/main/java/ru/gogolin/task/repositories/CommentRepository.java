package ru.gogolin.task.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gogolin.task.entities.Comment;
import ru.gogolin.task.entities.Task;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentsByTask(Task task, Pageable pageable);
    Optional<Comment> findCommentByTaskAndText(Task task, String text);
}

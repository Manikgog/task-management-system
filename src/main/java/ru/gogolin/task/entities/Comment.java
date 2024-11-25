package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "comments")
@SequenceGenerator(name = "comment_seq", sequenceName = "comment_sequence", allocationSize = 1)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    public Comment() {
    }

    public Comment(Task task, String text, User author) {
        this.task = task;
        this.text = text;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(task, comment.task) && Objects.equals(text, comment.text) && Objects.equals(author, comment.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, text, author);
    }
}

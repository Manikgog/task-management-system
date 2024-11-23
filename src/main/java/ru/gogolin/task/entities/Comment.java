package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}

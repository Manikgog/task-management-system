package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@SequenceGenerator(name = "task_seq", sequenceName = "task_sequence", allocationSize = 1)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    private long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

    public Task() {
    }

    public Task(String title,
                String description,
                Status status,
                User author,
                Priority priority,
                User executor) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.author = author;
        this.priority = priority;
        this.executor = executor;
    }


}

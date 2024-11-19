package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "priorities")
@SequenceGenerator(name = "priority_seq", sequenceName = "priority_sequence", allocationSize = 1)
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priority_seq")
    private Integer id;

    private String name;

    public Priority() {
    }

    public Priority(String name) {
        this.name = name;
    }

}

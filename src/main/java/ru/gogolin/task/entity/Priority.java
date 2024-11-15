package ru.gogolin.task.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "priorities")
@SequenceGenerator(name = "priority_seq", sequenceName = "priority_sequence", allocationSize = 1)
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priority_seq")
    private int id;

    private String name;

    public Priority() {
    }

    public Priority(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

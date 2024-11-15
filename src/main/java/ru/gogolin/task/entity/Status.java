package ru.gogolin.task.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
@SequenceGenerator(name = "status_seq", sequenceName = "status_sequence", allocationSize = 1)
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_seq")
    private int id;

    private String name;

    public Status() {
    }

    public Status(String status) {
        this.name = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

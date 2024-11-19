package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "statuses")
@SequenceGenerator(name = "status_seq", sequenceName = "status_sequence", allocationSize = 1)
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_seq")
    private Integer id;

    private String name;

    public Status() {
    }

    public Status(String status) {
        this.name = status;
    }
}

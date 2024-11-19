package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "roles")
@SequenceGenerator(name = "role_seq", sequenceName = "role_sequence", allocationSize = 1)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "role_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}

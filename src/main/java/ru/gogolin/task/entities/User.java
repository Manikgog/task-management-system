package ru.gogolin.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, roles);
    }
}

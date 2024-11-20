package ru.gogolin.task.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users_roles")
public class UsersRoles {

    @EmbeddedId
    private UsersRolesId id;

}

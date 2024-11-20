package ru.gogolin.task.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class UsersRolesId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "role_id")
    private Integer rolesId;

    public UsersRolesId() {}

    public UsersRolesId(Long userId, Integer rolesId) {
        this.userId = userId;
        this.rolesId = rolesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersRolesId that = (UsersRolesId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(rolesId, that.rolesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, rolesId);
    }
}

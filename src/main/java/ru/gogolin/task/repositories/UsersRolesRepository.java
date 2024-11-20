package ru.gogolin.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogolin.task.entities.UsersRoles;
import ru.gogolin.task.entities.UsersRolesId;

public interface UsersRolesRepository extends JpaRepository<UsersRoles, UsersRolesId> {
}

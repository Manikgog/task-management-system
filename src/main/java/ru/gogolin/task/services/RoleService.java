package ru.gogolin.task.services;

import ru.gogolin.task.entities.Role;

public interface RoleService {
    Role getRoleByName(String roleName);
}

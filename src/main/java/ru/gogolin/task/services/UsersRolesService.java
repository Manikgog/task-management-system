package ru.gogolin.task.services;

import org.springframework.stereotype.Service;
import ru.gogolin.task.entities.UsersRoles;
import ru.gogolin.task.entities.UsersRolesId;
import ru.gogolin.task.repositories.UsersRolesRepository;

@Service
public class UsersRolesService {

    private final UsersRolesRepository usersRolesRepository;

    public UsersRolesService(UsersRolesRepository usersRolesRepository) {
        this.usersRolesRepository = usersRolesRepository;
    }

    public UsersRoles saveUsersRole(Long userId, Integer roleId) {
        UsersRolesId usersRolesId = new UsersRolesId(userId, roleId);
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setId(usersRolesId);
        return usersRolesRepository.save(usersRoles);
    }

}

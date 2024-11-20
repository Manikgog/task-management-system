package ru.gogolin.task.services;

import org.springframework.stereotype.Service;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.exceptions.NotFoundException;
import ru.gogolin.task.repositories.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException(String.format("Роль %s не найдена.", roleName)));
    }
}

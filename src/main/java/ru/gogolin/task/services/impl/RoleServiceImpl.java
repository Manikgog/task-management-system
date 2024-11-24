package ru.gogolin.task.services.impl;

import org.springframework.stereotype.Service;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.exceptions.EntityNotFoundException;
import ru.gogolin.task.repositories.RoleRepository;
import ru.gogolin.task.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Роль %s не найдена.", roleName)));
    }
}

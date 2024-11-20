package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.gogolin.task.entities.Role;

@Service
@RequiredArgsConstructor
public class CheckAccessService {

    private final UserService userService;

    public boolean checkIsAdmin(Authentication authentication) {
        Role role = (Role) authentication.getAuthorities();
        if(role.equals("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }

}

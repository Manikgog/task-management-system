package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.gogolin.task.services.TaskService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckAccessService {
    private final UserService userService;
    private final TaskService taskService;

    /*public boolean isAdmin(Principal principal) {
        log.info("Was invoked method for verify of access");
        User user = userService.findByEmail(principal.getName());
        return user.getRoles().stream().map(Role::getName).toList().contains("ROLE_ADMIN");
    }*/

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN");
    }

}

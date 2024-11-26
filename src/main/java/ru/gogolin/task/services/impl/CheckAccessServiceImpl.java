package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.services.CheckAccessService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckAccessServiceImpl implements CheckAccessService {

    @Override
    @LogExecution
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN");
    }

}

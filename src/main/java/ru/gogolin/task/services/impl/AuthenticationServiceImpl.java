package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.AuthenticationUserDto;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.services.AuthenticationService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @LogExecution
    public AuthenticationUserDto findByEmailAndPassword(JwtRequest authRequest) {
        User user = userService.findByEmail(authRequest.email());
        if (user != null && passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            return new AuthenticationUserDto(
                    user.getUsername(),
                    (List<Role>) user.getRoles()
                    );
        }
        return null;
    }
}

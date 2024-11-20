package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.gogolin.task.dtos.AuthenticationUserDto;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.services.AuthenticationService;
import ru.gogolin.task.entities.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationUserDto findByEmailAndPassword(JwtRequest authRequest) {
        User user = userService.findByEmail(authRequest.email())
                .orElseThrow(() -> new NotFoundException(String.format("Указанный email %s не найден", authRequest.email())));
        if (user != null && passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            return new AuthenticationUserDto(
                    user.getUsername(),
                    (List<Role>) user.getRoles()
                    );
        }
        return null;
    }
}

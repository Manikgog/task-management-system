package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.dtos.AuthenticationUserDto;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.dtos.JwtResponse;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.services.AuthenticationService;
import ru.gogolin.task.services.impl.UserRegistrationService;
import ru.gogolin.task.services.impl.UserService;
import ru.gogolin.task.utils.JwtTokenUtils;

@Tag(name = "Authentication Controller", description = "API for working with authentication")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationService authenticationService;
    private final UserRegistrationService userRegistrationService;

    @Operation(summary = "Authenticate user. Create token.")
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        AuthenticationUserDto authenticationUserDto = authenticationService.findByEmailAndPassword(authRequest);
        if(authenticationUserDto == null) {
            throw new BadRequestException("Неправильный email или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.email());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationDto registrationDto) {
        userRegistrationService.createNewUser(registrationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody String email) {
        userService.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

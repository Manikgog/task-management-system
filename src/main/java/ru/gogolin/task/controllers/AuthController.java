package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.dtos.*;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.services.AuthenticationService;
import ru.gogolin.task.services.UserRegistrationService;
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
    public ResponseEntity<?> createAuthToken(@Valid @RequestBody JwtRequest authRequest) {
        AuthenticationUserDto authenticationUserDto = authenticationService.findByEmailAndPassword(authRequest);
        if(authenticationUserDto == null) {
            throw new BadRequestException("Incorrect email or password.");
        }
        String token = jwtTokenUtils.generateToken(authenticationUserDto);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(summary = "Registration user")
    @PostMapping("/user/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDto registrationDto) {
        userRegistrationService.createNewUser(registrationDto);
        return new ResponseEntity<>("User created.", HttpStatus.CREATED);
    }

    @Operation(summary = "Deletion of user")
    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody EmailDto email) {
        userService.deleteByEmail(email.email());
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }
}

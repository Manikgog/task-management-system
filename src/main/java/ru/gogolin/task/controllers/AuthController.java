package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.dtos.JwtResponse;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.services.UserRegistrationService;
import ru.gogolin.task.services.UserService;
import ru.gogolin.task.utils.JwtTokenUtils;

@Tag(name = "Authentication Controller", description = "API for working with authentication")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRegistrationService userRegistrationService;

    @Operation(summary = "Authenticate user. Create token.")
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
        }catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
}

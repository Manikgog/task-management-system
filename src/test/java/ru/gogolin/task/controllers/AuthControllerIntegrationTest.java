package ru.gogolin.task.controllers;

import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import ru.gogolin.task.dtos.EmailDto;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.User;
import java.util.List;
import java.util.Optional;

import static ru.gogolin.task.testdata.TestData.*;

public class AuthControllerIntegrationTest extends BaseApiControllerTest {

    @PostConstruct
    private void deletionDB(){
        usersRepository.deleteAll();
    }

    @BeforeEach
    public void init() {
        User admin = new User();
        admin.setUsername("admin@email.ru");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setName("admin");
        Role role = roleRepository.findByName("ROLE_ADMIN").get();
        role.setName("ROLE_ADMIN");
        admin.setRoles(List.of(role));
        usersRepository.save(admin);
        User user = new User();
        user.setUsername("user@email.ru");
        user.setPassword(passwordEncoder.encode("user"));
        user.setName("user");
        role = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(List.of(role));
        usersRepository.save(user);
    }

    @AfterEach
    public void clearDatabase() {
        usersRepository.deleteAll();
    }

    @Test
    public void registrationTest() {
        usersRepository.deleteAll();
        RegistrationDto registrationDto = new RegistrationDto(
                "test@email.com",
                "testpassword",
                "testpassword",
                "testName"
        );
        HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(registrationDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_REGISTRATION,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(USER_CREATED);
        Optional<User> newUser = usersRepository.findByUsername("test@email.com");
        Assertions.assertThat(newUser.isPresent()).isTrue();
        Assertions.assertThat(newUser.get().getName()).isEqualTo("testName");
    }

    @Test
    public void createAuthTokenTest() {
        JwtRequest request = new JwtRequest("admin@email.ru", "admin");
        HttpEntity<JwtRequest> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                AUTH_URL_TEMPLATE,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteTest() {
        JwtRequest credentials = new JwtRequest("admin@email.ru", "admin");
        HttpHeaders headers = getAuthHeader(credentials);
        EmailDto request = new EmailDto("user@email.ru");
        HttpEntity<EmailDto> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                DELETION_URL,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("User deleted.");
    }

}

package ru.gogolin.task.controllers;

import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gogolin.task.PostgresSQLTestContainerExtension;
import ru.gogolin.task.dtos.EmailDto;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.dtos.RegistrationDto;
import ru.gogolin.task.entities.Role;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.repositories.RoleRepository;
import ru.gogolin.task.repositories.UsersRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ru.gogolin.task.testdata.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest extends PostgresSQLTestContainerExtension {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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



    private HttpHeaders getAuthHeader(JwtRequest credentials) {
        HttpEntity<JwtRequest> requestHttpAuthEntity = new HttpEntity<>(credentials);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                AUTH_URL_TEMPLATE,
                HttpMethod.POST,
                requestHttpAuthEntity,
                new ParameterizedTypeReference<>() {
                });

        Map<String, String> responseMap = responseEntity.getBody();
        String token = "Bearer " + responseMap.get(AUTHENTICATION_RESPONSE_TOKEN_KEY);
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, token);
        return headers;
    }

}

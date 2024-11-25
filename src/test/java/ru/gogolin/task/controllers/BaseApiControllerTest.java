package ru.gogolin.task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gogolin.task.PostgresSQLTestContainerExtension;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.repositories.*;
import java.util.Map;
import java.util.Random;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ru.gogolin.task.testdata.TestData.AUTHENTICATION_RESPONSE_TOKEN_KEY;
import static ru.gogolin.task.testdata.TestData.AUTH_URL_TEMPLATE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseApiControllerTest extends PostgresSQLTestContainerExtension {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected StatusRepository statusRepository;

    @Autowired
    protected PrioritiesRepository prioritiesRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected final Random random = new Random();

    protected HttpHeaders getAuthHeader(JwtRequest credentials) {
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

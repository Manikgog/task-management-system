package ru.gogolin.task.controllers;

import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import ru.gogolin.task.dtos.*;
import ru.gogolin.task.entities.*;
import java.util.List;
import java.util.Optional;

import static ru.gogolin.task.testdata.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest extends BaseApiControllerTest {

    @PostConstruct
    public void checkUsers() {
        Role adminRole = roleRepository.findByName(ROLE_ADMIN).get();
        Role userRole = roleRepository.findByName(ROLE_USER).get();
        taskRepository.deleteAll();
        usersRepository.deleteAll();
        User admin = new User();
        admin.setUsername(ADMIN_EMAIL);
        admin.setName(ADMIN_NAME);
        admin.setPassword(ADMIN_PASSWORD);
        admin.setRoles(List.of(adminRole));
        usersRepository.save(admin);
        User user = new User();
        user.setUsername(USER_EMAIL);
        user.setName(USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setRoles(List.of(userRole));
        usersRepository.save(user);
    }

    @BeforeEach
    public void init(){
        List<Status> statuses = statusRepository.findAll();
        List<Priority> priorities = prioritiesRepository.findAll();
        for(int i = 0; i < statuses.size(); i++){
            Task task = createTask(i+1, statuses, priorities);
            taskRepository.save(task);
        }
    }

    private Task createTask(int number, List<Status> statuses, List<Priority> priorities) {
        Task task = new Task();
        task.setTitle("Title " + number);
        task.setDescription("Description " + number);
        task.setStatus(statuses.get(0));
        task.setPriority(priorities.get(0));
        task.setAuthor(usersRepository.findByUsername("admin@email.ru").get());
        task.setExecutor(usersRepository.findByUsername("user@email.ru").get());
        return task;
    }

    @AfterEach
    public void cleanUp(){
        taskRepository.deleteAll();
    }

    @Test
    public void createTaskTest(){
        taskRepository.deleteAll();
        JwtRequest credentials = new JwtRequest(ADMIN_EMAIL, ADMIN_NAME);
        HttpHeaders headers = getAuthHeader(credentials);
        TaskDto request = new TaskDto(
                "test title",
                "test description",
                "in process",
                "medium priority",
                USER_EMAIL,
                ADMIN_EMAIL
        );
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<TaskResponseDto> responseEntity = restTemplate.exchange(
                CREATE_TASK_TEMPLATE,
                HttpMethod.POST,
                requestEntity,
                TaskResponseDto.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody().author().email()).isEqualTo(ADMIN_EMAIL);
        Assertions.assertThat(responseEntity.getBody().executor().email()).isEqualTo(USER_EMAIL);
        Assertions.assertThat(responseEntity.getBody().description()).isEqualTo("test description");
        Assertions.assertThat(responseEntity.getBody().title()).isEqualTo("test title");
        Assertions.assertThat(responseEntity.getBody().status()).isEqualTo("in process");
        Assertions.assertThat(responseEntity.getBody().priority()).isEqualTo("medium priority");

        Task newTask = taskRepository.findByTitle("test title").get();
        Assertions.assertThat(newTask.getAuthor().getUsername()).isEqualTo(ADMIN_EMAIL);
        Assertions.assertThat(newTask.getTitle()).isEqualTo("test title");
        Assertions.assertThat(newTask.getExecutor().getUsername()).isEqualTo(USER_EMAIL);
        Assertions.assertThat(newTask.getStatus().getName()).isEqualTo("in process");
        Assertions.assertThat(newTask.getPriority().getName()).isEqualTo("medium priority");
    }


    @Test
    public void deletionTest() {
        Task taskToDelete = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        TitleDto request = new TitleDto(taskToDelete.getTitle());
        JwtRequest credentials = new JwtRequest(ADMIN_EMAIL, ADMIN_NAME);
        HttpHeaders headers = getAuthHeader(credentials);
        HttpEntity<TitleDto> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                DELETE_TASK_TEMPLATE,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Task deleted.");

        Assertions.assertThat(taskRepository.findByTitle(taskToDelete.getTitle())).isEqualTo(Optional.empty());
    }


    @Test
    public void getAllTasksTest(){
        JwtRequest credentials = new JwtRequest(ADMIN_EMAIL, ADMIN_NAME);
        HttpHeaders headers = getAuthHeader(credentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        int page = 0, size = 10;
        ResponseEntity<List<TaskResponseDto>> responseEntity = restTemplate.exchange(
                GET_ALL_TASK_TEMPLATE + "?page=" + page + "&size=" + size,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskResponseDto> tasks = responseEntity.getBody();
        Assertions.assertThat(tasks.size()).isEqualTo(taskRepository.findAll().size());
    }


    @Test
    public void getTasks() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        User user = usersRepository.findByUsername(task.getAuthor().getUsername()).get();
        JwtRequest credentials = new JwtRequest(user.getUsername(), user.getName());
        HttpHeaders headers = getAuthHeader(credentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        int page=0, size=10;
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<List<TaskResponseDto>> responseEntity = restTemplate.exchange(
                GET_YOUR_TASKS_TEMPLATE + "?page=" + page + "&size=" + size,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskResponseDto> tasks = responseEntity.getBody();
        Page<Task> tasksFromDB = taskRepository.findByAuthor_Username(user.getUsername(), pageable);
        Assertions.assertThat(tasks.size()).isEqualTo(tasksFromDB.get().toList().size());
    }


    @Test
    public void getTasksByAuthorTest() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        User user = usersRepository.findByUsername(task.getAuthor().getUsername()).get();
        JwtRequest credentials = new JwtRequest(user.getUsername(), user.getName());
        HttpHeaders headers = getAuthHeader(credentials);
        EmailDto requestBody = new EmailDto(user.getUsername());
        HttpEntity<EmailDto> requestEntity = new HttpEntity<>(requestBody, headers);
        int page=0, size=10;
        Pageable pageable = PageRequest.of(page, size);

        ResponseEntity<List<TaskResponseDto>> responseEntity = restTemplate.exchange(
                GET_TASKS_BY_AUTHOR_EMAIL_TEMPLATE + "?page=" + page + "&size=" + size,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskResponseDto> tasks = responseEntity.getBody();
        Page<Task> tasksFromDB = taskRepository.findByAuthor_Username(task.getAuthor().getUsername(), pageable);
        Assertions.assertThat(tasks.size()).isEqualTo(tasksFromDB.get().toList().size());
    }


    @Test
    public void getTasksByExecutorTest() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        User user = usersRepository.findByUsername(task.getExecutor().getUsername()).get();
        JwtRequest credentials = new JwtRequest(user.getUsername(), user.getName());
        HttpHeaders headers = getAuthHeader(credentials);
        EmailDto requestBody = new EmailDto(user.getUsername());
        HttpEntity<EmailDto> requestEntity = new HttpEntity<>(requestBody, headers);
        int page=0, size=10;
        Pageable pageable = PageRequest.of(page, size);

        ResponseEntity<List<TaskResponseDto>> responseEntity = restTemplate.exchange(
                GET_TASKS_BY_EXECUTOR_EMAIL_TEMPLATE + "?page=" + page + "&size=" + size,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskResponseDto> tasks = responseEntity.getBody();
        Page<Task> tasksFromDB = taskRepository.findByExecutor_Username(user.getUsername(), pageable);
        Assertions.assertThat(tasks.size()).isEqualTo(tasksFromDB.get().toList().size());
    }

    @Test
    public void changeStatusTest() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        User executor = usersRepository.findByUsername(task.getExecutor().getUsername()).get();
        JwtRequest credentials = new JwtRequest(executor.getUsername(), executor.getName());
        HttpHeaders headers = getAuthHeader(credentials);
        TaskStatusDto requestBody = new TaskStatusDto(task.getTitle(), "completed");
        HttpEntity<TaskStatusDto> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<TaskResponseDto> responseEntity = restTemplate.exchange(
                CHANGE_STATUS_TASK_TEMPLATE,
                HttpMethod.POST,
                requestEntity,
                TaskResponseDto.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().status()).isEqualTo("completed");
    }

    @Test
    public void changeTaskPriorityTest() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        JwtRequest credentials = new JwtRequest(ADMIN_EMAIL, ADMIN_NAME);
        HttpHeaders headers = getAuthHeader(credentials);
        TaskPriorityDto requestBody = new TaskPriorityDto(task.getTitle(), "low priority");
        HttpEntity<TaskPriorityDto> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<TaskResponseDto> responseEntity = restTemplate.exchange(
                CHANGE_TASK_TEMPLATE,
                HttpMethod.POST,
                requestEntity,
                TaskResponseDto.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().priority()).isEqualTo("low priority");
    }


    @Test
    public void changeTaskExecutorTest() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        JwtRequest credentials = new JwtRequest(ADMIN_EMAIL, ADMIN_NAME);
        HttpHeaders headers = getAuthHeader(credentials);
        TaskExecutorDto requestBody = new TaskExecutorDto(task.getTitle(), ADMIN_EMAIL);
        HttpEntity<TaskExecutorDto> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<TaskResponseDto> responseEntity = restTemplate.exchange(
                CHANGE_EXECUTOR_TEMPLATE,
                HttpMethod.POST,
                requestEntity,
                TaskResponseDto.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().executor().email()).isEqualTo(ADMIN_EMAIL);
        Assertions.assertThat(responseEntity.getBody().executor().name()).isEqualTo(ADMIN_NAME);
    }

    @Test
    public void getTaskTest() {
        Task task = taskRepository.findAll().get(random.nextInt(taskRepository.findAll().size()));
        JwtRequest credentials = new JwtRequest(ADMIN_EMAIL, ADMIN_NAME);
        HttpHeaders headers = getAuthHeader(credentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<TaskResponseDto> responseEntity = restTemplate.exchange(
                GET_BY_TITLE_TEMPLATE + "?title=" + task.getTitle(),
                HttpMethod.GET,
                requestEntity,
                TaskResponseDto.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().title()).isEqualTo(task.getTitle());
        Assertions.assertThat(responseEntity.getBody().description()).isEqualTo(task.getDescription());
        Assertions.assertThat(responseEntity.getBody().priority()).isEqualTo(task.getPriority().getName());
        Assertions.assertThat(responseEntity.getBody().executor().email()).isEqualTo(USER_EMAIL);
        Assertions.assertThat(responseEntity.getBody().executor().name()).isEqualTo(USER_NAME);
        Assertions.assertThat(responseEntity.getBody().author().email()).isEqualTo(ADMIN_EMAIL);
        Assertions.assertThat(responseEntity.getBody().author().name()).isEqualTo(ADMIN_NAME);
    }
}

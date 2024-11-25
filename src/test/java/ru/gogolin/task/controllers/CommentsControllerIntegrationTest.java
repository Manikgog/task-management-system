package ru.gogolin.task.controllers;

import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.gogolin.task.dtos.CommentDto;
import ru.gogolin.task.dtos.CommentResponseDto;
import ru.gogolin.task.dtos.JwtRequest;
import ru.gogolin.task.entities.*;
import java.util.List;

import static ru.gogolin.task.testdata.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentsControllerIntegrationTest extends BaseApiControllerTest {

    @PostConstruct
    public void checkUsers() {
        Role adminRole = roleRepository.findByName(ROLE_ADMIN).get();
        Role userRole = roleRepository.findByName(ROLE_USER).get();
        taskRepository.deleteAll();
        usersRepository.deleteAll();
        User admin = new User();
        admin.setUsername(ADMIN_EMAIL);
        admin.setName(ADMIN_NAME);
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(List.of(adminRole));
        usersRepository.save(admin);
        User user = new User();
        user.setUsername(USER_EMAIL);
        user.setName(USER_NAME);
        user.setPassword(passwordEncoder.encode("user"));
        user.setRoles(List.of(userRole));
        usersRepository.save(user);
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
        task.setStatus(statuses.get(random.nextInt(statuses.size())));
        task.setPriority(priorities.get(random.nextInt(priorities.size())));
        task.setAuthor(usersRepository.findByUsername("admin@email.ru").get());
        task.setExecutor(usersRepository.findByUsername("user@email.ru").get());
        return task;
    }

    @BeforeEach
    public void init(){
        List<Task> tasks = taskRepository.findAll();
        for(int i = 0; i < tasks.size(); i++){
            Comment comment = createComment(i+1, tasks.get(i));
            commentRepository.save(comment);
        }
    }

    private Comment createComment(int number, Task task){
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setAuthor(usersRepository.findByUsername(USER_EMAIL).get());
        comment.setText("test comment " + number);
        return comment;
    }

    @AfterEach
    public void cleanUp(){
        commentRepository.deleteAll();
    }

    @Test
    public void createCommentTest() {
        commentRepository.deleteAll();
        List<Task> tasks = taskRepository.findAll();
        String commentText = "Comment text";
        CommentDto requestBody = new CommentDto(tasks.get(0).getTitle(), commentText);
        JwtRequest credentials = new JwtRequest(tasks.get(tasks.size()-1).getAuthor().getUsername(), tasks.get(tasks.size()-1).getAuthor().getName());
        HttpHeaders headers = getAuthHeader(credentials);
        HttpEntity<CommentDto> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<CommentResponseDto> responseEntity = restTemplate.exchange(
                COMMENT_URL_TEMPLATE,
                HttpMethod.POST,
                requestEntity,
                CommentResponseDto.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody().comment()).isEqualTo(commentText);
        Assertions.assertThat(responseEntity.getBody().taskTitle()).isEqualTo(tasks.get(0).getTitle());
        Assertions.assertThat(responseEntity.getBody().author().email()).isEqualTo(tasks.get(0).getAuthor().getUsername());

        Comment newComment = commentRepository.findCommentByTaskAndText(tasks.get(0), commentText).get();
        Assertions.assertThat(responseEntity.getBody().taskTitle()).isEqualTo(newComment.getTask().getTitle());
        Assertions.assertThat(responseEntity.getBody().author().email()).isEqualTo(newComment.getAuthor().getUsername());
        Assertions.assertThat(responseEntity.getBody().comment()).isEqualTo(commentText);
        Assertions.assertThat(responseEntity.getBody().author().name()).isEqualTo(newComment.getAuthor().getName());
    }


    @Test
    public void getCommentsByTitleTest() {
        List<Comment> comments = commentRepository.findAll();
        User user = usersRepository.findByUsername(ADMIN_EMAIL).get();
        JwtRequest credentials = new JwtRequest(user.getUsername(), user.getName());
        HttpHeaders headers = getAuthHeader(credentials);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String taskTitle = comments.get(0).getTask().getTitle();
        ResponseEntity<List<CommentResponseDto>> responseEntity = restTemplate.exchange(
                COMMENT_URL_TEMPLATE + "/get?page=0&size=10&title=" + taskTitle,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
        comments = comments.stream().filter(c -> c.getTask().getTitle().equals(taskTitle)).toList();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().size()).isEqualTo(comments.size());
    }


    @Test
    public void deleteCommentTest() {
        List<Comment> comments = commentRepository.findAll();
        List<Task> tasks = taskRepository.findAll();
        User user = usersRepository.findByUsername(USER_EMAIL).get();
        JwtRequest credentials = new JwtRequest(user.getUsername(), user.getName());
        HttpHeaders headers = getAuthHeader(credentials);
        CommentDto requestBody = new CommentDto(comments.get(0).getTask().getTitle(), comments.get(0).getText());
        HttpEntity<CommentDto> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                COMMENT_URL_TEMPLATE,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Comment deleted");
    }


}

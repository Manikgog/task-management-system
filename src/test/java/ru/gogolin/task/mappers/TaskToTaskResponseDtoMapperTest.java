package ru.gogolin.task.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gogolin.task.dtos.TaskResponseDto;
import ru.gogolin.task.entities.*;
import java.util.List;

import static ru.gogolin.task.testdata.TestData.ROLE_ADMIN;
import static ru.gogolin.task.testdata.TestData.ROLE_USER;

public class TaskToTaskResponseDtoMapperTest {

    private final TaskToTaskResponseDtoMapper mapper = new TaskToTaskResponseDtoMapper();

    @Test
    public void applyTestWithExecutor() {
        Task task = createTask(createExecutor());
        TaskResponseDto taskResponseDto = mapper.apply(task);
        Assertions.assertEquals(taskResponseDto.executor().email(), task.getExecutor().getUsername());
        Assertions.assertEquals(taskResponseDto.executor().name(), task.getExecutor().getName());
        Assertions.assertEquals(taskResponseDto.author().email(), task.getAuthor().getUsername());
        Assertions.assertEquals(taskResponseDto.author().name(), task.getAuthor().getName());
        Assertions.assertEquals(taskResponseDto.description(), task.getDescription());
        Assertions.assertEquals(taskResponseDto.title(), task.getTitle());
        Assertions.assertEquals(taskResponseDto.status(), task.getStatus().getName());
    }

    @Test
    public void applyTestWithoutExecutor() {
        Task task = createTask(null);
        TaskResponseDto taskResponseDto = mapper.apply(task);
        Assertions.assertEquals(taskResponseDto.author().email(), task.getAuthor().getUsername());
        Assertions.assertEquals(taskResponseDto.author().name(), task.getAuthor().getName());
        Assertions.assertEquals(taskResponseDto.description(), task.getDescription());
        Assertions.assertEquals(taskResponseDto.title(), task.getTitle());
        Assertions.assertEquals(taskResponseDto.status(), task.getStatus().getName());
    }

    private Task createTask(User executor) {
        User author = createAuthor();
        Task task = new Task();
        task.setId(1);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setStatus(new Status("completed"));
        task.setAuthor(author);
        task.setPriority(createPriority());
        task.setExecutor(executor);
        return task;
    }

    private Priority createPriority() {
        Priority priority = new Priority();
        priority.setId(1);
        priority.setName("high priority");
        return priority;
    }

    private User createAuthor(){
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName(ROLE_ADMIN);
        User author = new User();
        author.setId(1L);
        author.setUsername("author@email.com");
        author.setPassword("AuthorPassword");
        author.setUsername("AuthorName");
        author.setRoles(List.of(adminRole));
        return author;
    }

    private User createExecutor(){
        Role userRole = new Role();
        userRole.setId(2);
        userRole.setName(ROLE_USER);
        User executor = new User();
        executor.setId(2L);
        executor.setUsername("executor@email.com");
        executor.setPassword("ExecutorPassword");
        executor.setUsername("ExecutorName");
        executor.setRoles(List.of(userRole));
        return executor;
    }

}

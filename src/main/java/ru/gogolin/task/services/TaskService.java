package ru.gogolin.task.services;

import org.springframework.security.core.Authentication;
import ru.gogolin.task.dtos.TaskDto;
import ru.gogolin.task.dtos.TaskResponseDto;
import ru.gogolin.task.entities.Task;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    TaskResponseDto addTask(TaskDto taskDto);

    void deleteTask(String title);

    TaskResponseDto getTask(String title, String email);

    List<TaskResponseDto> getAllTasks(int page, int size);

    List<TaskResponseDto> getTasks(Principal principal, int page, int size);

    List<TaskResponseDto> getTasksByExecutor(String executorEmail, int page, int size);

    List<TaskResponseDto> getTasksByAuthor(String authorEmail, int page, int size);

    Task getTaskByTitle(String title);

    TaskResponseDto changeStatus(TaskDto taskDto, Authentication authentication);

    TaskResponseDto changeTask(TaskDto taskDto, Authentication authentication);
}

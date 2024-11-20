package ru.gogolin.task.services;

import ru.gogolin.task.dtos.TaskDto;

import java.security.Principal;

public interface TaskService {
    void addTask(TaskDto taskDto, String username);
}

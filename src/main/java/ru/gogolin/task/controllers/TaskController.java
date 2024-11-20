package ru.gogolin.task.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.dtos.TaskDto;
import ru.gogolin.task.services.TaskService;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDto taskDto, Principal principal) {
        String email = principal.getName();
        taskService.addTask(taskDto, email);
        return ResponseEntity.ok("Task created");
    }

}

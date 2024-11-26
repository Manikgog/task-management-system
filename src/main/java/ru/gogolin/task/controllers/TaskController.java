package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.dtos.*;
import ru.gogolin.task.services.TaskService;
import java.security.Principal;
import java.util.List;

@Tag(name = "Task Controller", description = "API for working with tasks")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Creation task")
    @LogExecution
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.addTask(taskDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Deletion task")
    @LogExecution
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @DeleteMapping
    public ResponseEntity<String> deleteTask(@Valid @RequestBody TitleDto title) {
        taskService.deleteTask(title.title());
        return ResponseEntity.ok("Task deleted.");
    }

    @Operation(summary = "Search for a task by title")
    @LogExecution
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @GetMapping("/getByTitle")
    public ResponseEntity<TaskResponseDto> getTask(@Valid @RequestBody TitleDto title, Principal principal) {
        return ResponseEntity.ok(taskService.getTask(title.title(), principal.getName()));
    }

    @Operation(summary = "Getting all the tasks")
    @LogExecution
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @GetMapping("/getAll")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getAllTasks(page, size));
    }

    @Operation(summary = "Getting your tasks")
    @LogExecution
    @GetMapping("/getYourTasks")
    public ResponseEntity<List<TaskResponseDto>> getTasks(@RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size,
                                                                  Principal principal) {
        return ResponseEntity.ok(taskService.getTasks(principal, page, size));
    }

    @Operation(summary = "Getting tasks by author")
    @LogExecution
    @PostMapping("/getByAuthorEmail")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAuthor(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                                                  @Valid @RequestBody EmailDto authorEmail) {
        return ResponseEntity.ok(taskService.getTasksByAuthor(authorEmail.email(), page, size));
    }

    @Operation(summary = "Getting tasks by executor")
    @LogExecution
    @PostMapping("/getByExecutorEmail")
    public ResponseEntity<List<TaskResponseDto>> getTasksByExecutor(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "10") int size,
                                                                    @Valid @RequestBody EmailDto executorEmail) {
        return ResponseEntity.ok(taskService.getTasksByExecutor(executorEmail.email(), page, size));
    }

    @Operation(summary = "Changing status of task")
    @LogExecution
    @PostMapping("/changeStatus")
    public ResponseEntity<TaskResponseDto> changeStatus(@Valid @RequestBody TaskStatusDto taskDto, Authentication authentication) {
        return ResponseEntity.ok(taskService.changeStatus(taskDto, authentication));
    }

    @Operation(summary = "Changing task")
    @LogExecution
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @PostMapping("/changePriority")
    public ResponseEntity<TaskResponseDto> changeTaskPriority(@Valid @RequestBody TaskPriorityDto taskDto) {
        return ResponseEntity.ok(taskService.changeTask(taskDto));
    }

    @Operation(summary = "Changing executor")
    @LogExecution
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @PostMapping("/changeExecutor")
    public ResponseEntity<TaskResponseDto> changeTaskPriority(@Valid @RequestBody TaskExecutorDto taskDto) {
        return ResponseEntity.ok(taskService.changeTaskExecutor(taskDto));
    }

}

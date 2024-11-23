package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.gogolin.task.dtos.TaskDto;
import ru.gogolin.task.dtos.TaskResponseDto;
import ru.gogolin.task.services.TaskService;
import java.security.Principal;
import java.util.List;

@RestController("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Creation task.")
    @PostMapping("/create")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.addTask(taskDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Deletion task.")
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteTask(@PathVariable(name = "title") String title) {
        taskService.deleteTask(title);
        return ResponseEntity.ok("Task deleted");
    }

    @Operation(summary = "Search for a task by title.")
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @GetMapping("/{title}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable(name = "title") String title, Principal principal) {
        return ResponseEntity.ok(taskService.getTask(title, principal.getName()));
    }

    @Operation(summary = "Getting all the tasks.")
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @GetMapping("/getAll")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getAllTasks(page, size));
    }

    @Operation(summary = "Getting your tasks.")
    @GetMapping("/email/author")
    public ResponseEntity<List<TaskResponseDto>> getTasks(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  Principal principal) {
        return ResponseEntity.ok(taskService.getTasks(principal, page, size));
    }

    @Operation(summary = "Getting tasks by author.")
    @GetMapping("/{authorEmail}/author")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAuthor(@PathVariable String authorEmail,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasksByAuthor(authorEmail, page, size));
    }

    @Operation(summary = "Getting tasks by executor.")
    @GetMapping("/{executorEmail}/executor")
    public ResponseEntity<List<TaskResponseDto>> getTasksByExecutor(@PathVariable String executorEmail,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasksByExecutor(executorEmail, page, size));
    }

    @Operation(summary = "Changing status of task.")
    @PatchMapping("/{title}")
    public ResponseEntity<TaskResponseDto> changeStatus(@RequestBody TaskDto taskDto, Authentication authentication) {
        return ResponseEntity.ok(taskService.changeStatus(taskDto, authentication));
    }

    @Operation(summary = "Changing task.")
    @PreAuthorize("@checkAccessService.isAdmin(authentication)")
    @PatchMapping
    public ResponseEntity<TaskResponseDto> changeTask(@RequestBody TaskDto taskDto, Authentication authentication) {
        return ResponseEntity.ok(taskService.changeTask(taskDto, authentication));
    }

}

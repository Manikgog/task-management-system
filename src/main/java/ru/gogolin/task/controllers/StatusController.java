package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.services.StatusService;
import java.util.List;

@Tag(name = "Status Controller", description = "API for working with statuses")
@RestController
@RequestMapping("/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @Operation(summary = "Getting a list of statuses")
    @LogExecution
    @GetMapping
    public ResponseEntity<List<String>> getStatuses() {
        return ResponseEntity.ok(statusService.getAll());
    }
}

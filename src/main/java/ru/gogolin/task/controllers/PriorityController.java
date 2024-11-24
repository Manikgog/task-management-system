package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.services.PriorityService;
import java.util.List;

@Tag(name = "Priority Controller", description = "API for working with priorities")
@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @Operation(summary = "Getting a list of priorities")
    @GetMapping
    public ResponseEntity<List<String>> getPriorities() {
        return ResponseEntity.ok(priorityService.getAll());
    }

}

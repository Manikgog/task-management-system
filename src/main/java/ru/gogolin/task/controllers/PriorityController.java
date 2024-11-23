package ru.gogolin.task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.services.PriorityService;

import java.util.List;

@RestController("/priority")
public class PriorityController {

    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getPriorities() {
        return ResponseEntity.ok(priorityService.getAll());
    }

}

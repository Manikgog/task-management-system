package ru.gogolin.task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gogolin.task.services.StatusService;

import java.util.List;

@RestController("/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getStatuses() {
        return ResponseEntity.ok(statusService.getAll());
    }
}

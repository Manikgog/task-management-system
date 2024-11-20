package ru.gogolin.task.dtos;

public record TaskDto(
        String title,
        String description,
        String status,
        String priority,
        String comment,
        String executor
) {}

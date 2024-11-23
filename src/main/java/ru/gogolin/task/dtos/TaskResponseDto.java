package ru.gogolin.task.dtos;

public record TaskResponseDto(
        String title,
        String description,
        String status,
        String priority,
        UserDto author,
        UserDto executor
) {
}

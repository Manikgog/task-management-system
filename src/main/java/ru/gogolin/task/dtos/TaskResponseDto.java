package ru.gogolin.task.dtos;

import java.io.Serializable;

public record TaskResponseDto (
        String title,
        String description,
        String status,
        String priority,
        UserDto author,
        UserDto executor
) implements Serializable {
}

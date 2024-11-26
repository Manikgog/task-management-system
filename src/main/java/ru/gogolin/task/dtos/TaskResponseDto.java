package ru.gogolin.task.dtos;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record TaskResponseDto (
        String title,
        String description,
        String status,
        String priority,
        UserDto author,
        UserDto executor
) implements Serializable {
}

package ru.gogolin.task.dtos;

import java.io.Serializable;

public record CommentResponseDto(
        String taskTitle,
        String comment,
        UserDto author) implements Serializable {
}

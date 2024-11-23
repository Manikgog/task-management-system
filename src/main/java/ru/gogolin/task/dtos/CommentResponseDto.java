package ru.gogolin.task.dtos;

public record CommentResponseDto(
        String taskTitle,
        String comment,
        UserDto author) {
}

package ru.gogolin.task.dtos;

import java.util.List;

public record UserDto(
        String email,
        String name,
        List<String> roles
) {
}

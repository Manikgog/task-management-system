package ru.gogolin.task.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(
        String email,
        String name,
        List<String> roles
) {
}

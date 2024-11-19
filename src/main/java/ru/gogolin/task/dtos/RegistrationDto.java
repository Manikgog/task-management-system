package ru.gogolin.task.dtos;

public record RegistrationDto(String username,
                              String password,
                              String confirmPassword,
                              String email) {
}

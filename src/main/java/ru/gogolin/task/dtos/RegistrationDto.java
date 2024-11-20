package ru.gogolin.task.dtos;

public record RegistrationDto(String email,
                              String password,
                              String confirmPassword,
                              String name) {
}

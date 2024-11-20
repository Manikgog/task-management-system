package ru.gogolin.task.dtos;

import ru.gogolin.task.entities.Role;
import java.io.Serializable;
import java.util.List;

public record AuthenticationUserDto(String email, List<Role> roles) implements Serializable {
}

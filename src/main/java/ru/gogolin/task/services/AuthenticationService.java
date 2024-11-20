package ru.gogolin.task.services;

import ru.gogolin.task.dtos.AuthenticationUserDto;
import ru.gogolin.task.dtos.JwtRequest;

public interface AuthenticationService {

    AuthenticationUserDto findByEmailAndPassword(JwtRequest requestDto);

}

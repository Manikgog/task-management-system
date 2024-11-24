package ru.gogolin.task.services;

import ru.gogolin.task.dtos.RegistrationDto;

public interface UserRegistrationService {
    void createNewUser(RegistrationDto registrationDto);
}

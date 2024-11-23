package ru.gogolin.task.services;

import ru.gogolin.task.entities.Status;

import java.util.List;

public interface StatusService {

    Status getStatus(String status);

    List<String> getAll();

}

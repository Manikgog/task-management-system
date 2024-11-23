package ru.gogolin.task.services;

import ru.gogolin.task.entities.Priority;

import java.util.List;

public interface PriorityService {
    Priority getPriority(String priority);

    List<String> getAll();

}

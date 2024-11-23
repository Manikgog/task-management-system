package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gogolin.task.entities.Priority;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.repositories.PrioritiesRepository;
import ru.gogolin.task.services.PriorityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PrioritiesRepository prioritiesRepository;

    @Override
    public Priority getPriority(String priority) {
        return prioritiesRepository.findPriorityByName(priority.trim())
                .orElseThrow(() -> new BadRequestException(String.format("Приоритет с названием %s не найден", priority)));
    }

    @Override
    public List<String> getAll() {
        return prioritiesRepository.findAll().stream().map(Priority::getName).toList();
    }
}

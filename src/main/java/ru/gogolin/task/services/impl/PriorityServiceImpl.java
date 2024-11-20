package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.gogolin.task.entities.Priority;
import ru.gogolin.task.repositories.PrioritiesRepository;
import ru.gogolin.task.services.PriorityService;

@Service
@RequiredArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PrioritiesRepository prioritiesRepository;

    @Override
    public Priority getPriority(String priority) {
        return prioritiesRepository.findPriorityByName(priority)
                .orElseThrow(() -> new NotFoundException(String.format("Приоритет с названием %s не найден", priority)));
    }
}

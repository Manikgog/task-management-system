package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gogolin.task.entities.Status;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.repositories.StatusRepository;
import ru.gogolin.task.services.StatusService;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status getStatus(String status) {
        return statusRepository.findStatusByName(status.trim())
                .orElseThrow(() -> new BadRequestException(String.format("Статус %s не найден", status)));
    }
}

package ru.gogolin.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gogolin.task.annotations.LogException;
import ru.gogolin.task.annotations.LogExecution;
import ru.gogolin.task.entities.Status;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.repositories.StatusRepository;
import ru.gogolin.task.services.StatusService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    @LogExecution
    @LogException
    public Status getStatus(String status) {
        return statusRepository.findStatusByName(status.trim())
                .orElseThrow(() -> new BadRequestException(String.format("Status %s not found", status)));
    }

    @Override
    @LogExecution
    public List<String> getAll() {
        return statusRepository.findAll().stream().map(Status::getName).toList();
    }
}

package ru.gogolin.task.services;

import org.springframework.security.core.Authentication;

public interface CheckAccessService {
    boolean isAdmin(Authentication authentication);
}

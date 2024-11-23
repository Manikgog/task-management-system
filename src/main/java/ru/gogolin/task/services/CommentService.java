package ru.gogolin.task.services;

import org.springframework.security.core.Authentication;
import ru.gogolin.task.dtos.CommentDto;
import ru.gogolin.task.dtos.CommentResponseDto;
import java.security.Principal;
import java.util.List;

public interface CommentService {

    CommentResponseDto addComment(CommentDto commentDto, Authentication authentication);

    void deleteComment(Long commentId, Principal principal);

    public List<CommentResponseDto> getComments(String taskTitle, Principal principal, int page, int size);
}

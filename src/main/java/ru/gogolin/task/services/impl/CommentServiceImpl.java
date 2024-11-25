package ru.gogolin.task.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.gogolin.task.dtos.CommentDto;
import ru.gogolin.task.dtos.CommentResponseDto;
import ru.gogolin.task.entities.Comment;
import ru.gogolin.task.entities.Task;
import ru.gogolin.task.entities.User;
import ru.gogolin.task.exceptions.BadRequestException;
import ru.gogolin.task.exceptions.IsNotExecutorException;
import ru.gogolin.task.mappers.UserToUserDtoMapper;
import ru.gogolin.task.repositories.CommentRepository;
import ru.gogolin.task.services.CommentService;
import ru.gogolin.task.services.TaskService;
import java.security.Principal;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final UserToUserDtoMapper userToUserDtoMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              TaskService taskService,
                              UserService userService,
                              UserToUserDtoMapper userToUserDtoMapper) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
        this.userToUserDtoMapper = userToUserDtoMapper;
    }

    @Override
    public CommentResponseDto addComment(CommentDto commentDto, Authentication authentication) throws BadRequestException {
        User authorOfComment = userService.findByEmail(authentication.getPrincipal().toString());
        Task task = taskService.getTaskByTitle(commentDto.taskTitle());
        if(authorOfComment.equals(task.getAuthor()) ||
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {
            Comment comment = new Comment(task, commentDto.comment(), authorOfComment);
            Comment newComment = commentRepository.save(comment);
            return new CommentResponseDto(newComment.getTask().getTitle(),
                    newComment.getText(),
                    userToUserDtoMapper.apply(authorOfComment));
        }
        throw new IsNotExecutorException("You can leave comments only on your tasks.");
    }

    @Override
    public void deleteComment(CommentDto commentDto, Authentication authentication) {
        User authorOfComment = userService.findByEmail(authentication.getPrincipal().toString());
        Task task = taskService.getTaskByTitle(commentDto.taskTitle());
        Comment commentToDelete = commentRepository.findCommentByTaskAndText(task, commentDto.comment())
                        .orElseThrow(() -> new BadRequestException(String.format("Comment with text=\"%s\" not found", commentDto.comment())));
        if(commentToDelete.getAuthor().equals(authorOfComment) ||
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {
            commentRepository.delete(commentToDelete);
            return;
        }
        throw new IsNotExecutorException("Only the author of the comment or the administrator can delete the comment.");
    }

    @Override
    public List<CommentResponseDto> getComments(String taskTitle, Principal principal, int page, int size) {
        Task task = taskService.getTaskByTitle(taskTitle);
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findCommentsByTask(task, pageable);
        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getTask().getTitle(),
                        comment.getText(),
                        userToUserDtoMapper.apply(comment.getAuthor())
                )).toList();
    }

}

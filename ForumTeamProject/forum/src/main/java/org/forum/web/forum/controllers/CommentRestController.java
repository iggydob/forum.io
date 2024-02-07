package org.forum.web.forum.controllers;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.CommentMapper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.contracts.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    public static final String OPERATION_NOT_FOUND = "Operation not found!";
    private final CommentService service;
    private final AuthenticationHelper authenticationHelper;

    private final CommentMapper commentMapper;

    public CommentRestController(CommentService service, AuthenticationHelper authenticationHelper, CommentMapper commentMapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public List<Comment> get(@RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Comment get(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/count/{id}/{reactionType}")
    public long getCommentLikeCount(@PathVariable int id, @PathVariable String reactionType) {
        try {
            switch (reactionType) {
                case "likes":
                    return service.commentLikesCount(id);
                case "dislikes":
                    return service.commentDislikesCount(id);
                default:
                    throw new EntityNotFoundException(OPERATION_NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/post/{postId}")
    public List<Comment> getPostComments(@PathVariable int postId,@RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getPostComments(postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{postId}")
    public Comment createComment(@RequestHeader(name = "Credentials") String credentials, @PathVariable int postId,
                                 @Valid @RequestBody CommentDTO commentDTO) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Comment comment = commentMapper.fromDto(postId, commentDTO);
            service.create(user, comment);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Comment updateComment(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id,
                                 @Valid @RequestBody CommentDTO commentDTO) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Comment comment = commentMapper.fromDto(commentDTO, id);
            service.update(user, comment);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            service.delete(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{reactionType}/{commentId}")
    public void likeComment(@RequestHeader(name = "Credentials") String credentials, @PathVariable String reactionType,
                            @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            switch (reactionType) {
                case "like":
                    service.likeComment(commentId, user);
                    return;
                case "dislike":
                    service.dislikeComment(commentId, user);
                    return;
                case "delete":
                    service.deleteReaction(commentId, user);
                    return;
                default:
                    throw new EntityNotFoundException(OPERATION_NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}

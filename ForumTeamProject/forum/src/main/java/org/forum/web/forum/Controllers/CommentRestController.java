package org.forum.web.forum.Controllers;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.CommentMapper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.models.Dtos.PostDto;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService service;
    private final AuthenticationHelper authenticationHelper;

    private final CommentMapper commentMapper;

    @Autowired
    public CommentRestController(CommentService service, AuthenticationHelper authenticationHelper, CommentMapper commentMapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public List<Comment> get (@RequestHeader HttpHeaders headers){
        try {
            return service.getAll();
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Comment get(@PathVariable int id, @RequestHeader HttpHeaders headers) {

        try {
            return service.getById(id);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

//    @PostMapping("/{postid}/comments")
//    public Comment createComment(@RequestHeader HttpHeaders headers, @PathVariable int postid, @Valid @RequestBody CommentDTO commentDTO){
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            Comment comment = commentMapper.fromDto(commentDTO);
////            service.create();
//            //TODO per ID
//        } catch (EntityNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }

//    @PutMapping("/{id}")
//    public Post updateComment(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CommentDTO commentDTO) {
//
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            Comment comment = commentMapper.fromDto(commentDTO);
//            service.update();
//            //TODO per ID
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }

//    @DeleteMapping("/{id}")
//    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            service.delete();
//            //TODO per ID
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }

    @PostMapping("/likes/{id}")
    public void likeComment(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.likeComment(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/likes/{id}")
    public void dislikeComment(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.dislikeComment(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

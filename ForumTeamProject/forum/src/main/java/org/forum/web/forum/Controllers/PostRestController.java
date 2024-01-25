package org.forum.web.forum.Controllers;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.PostMapper;
import org.forum.web.forum.models.Dtos.PostDto;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService service;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;

    public PostRestController(PostService service, AuthenticationHelper authenticationHelper, PostMapper postMapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
    }

    //todo think of which get method u will need to use???
    //    @GetMapping
//    public List<Post> getAll() {
//        return service.getAll();
//    }
    @GetMapping("/recent")
    public List<Post>getMostRecent(){
        return service.getMostRecent();
    }
    @GetMapping("/commented")
    public List<Post>getMostCommented(){
        return service.getMostCommented();
    }
    //todo check endpoint
    @GetMapping("/search")
    public List<Post> getAllFiltered(@RequestParam(required = false) String title,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
        PostFilterOptions postFilterOptions = new PostFilterOptions(title, author, sortBy, sortOrder);
        return service.getFiltered(postFilterOptions);
    }

    @GetMapping("/{id}")
    public Post getId(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Post create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDto(postDto);
            service.create(post, user);
            return post;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Post update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDto(id, postDto);
            service.update(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        Post post = service.getById(id);

        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(post, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

package org.forum.web.forum.controllers.rest;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.exceptions.UnauthorizedOperationException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.PostMapper;
import org.forum.web.forum.helpers.mappers.TagMapper;
import org.forum.web.forum.models.Dtos.PostDto;
import org.forum.web.forum.models.Dtos.TagDto;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.repository.contracts.PostRepository;
import org.forum.web.forum.service.contracts.PostService;
import org.forum.web.forum.service.contracts.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService service;
    private final PostRepository repository;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final TagService tagService;

    public PostRestController(PostService service, PostRepository repository, AuthenticationHelper authenticationHelper, PostMapper postMapper, TagMapper tagMapper, TagService tagService) {
        this.service = service;
        this.repository = repository;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
    }

    @GetMapping
    public List<Post> getAllFiltered(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestHeader(name = "Credentials") String credentials) {
        try {
            authenticationHelper.tryGetUser(credentials);
            PostFilterOptions postFilterOptions = new PostFilterOptions(title, author, sortBy, sortOrder);
            return service.getFiltered(postFilterOptions);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public List<Post> getByUserId(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @PathVariable int id,
            @RequestHeader(name = "Credentials") String credentials) {
        try {
            PostFilterOptions filterOptions = new PostFilterOptions(title, null, sortBy, sortOrder);
            authenticationHelper.tryGetUser(credentials);
            return service.getByUserId(filterOptions, id);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/count")
    public long getCount() {
        return repository.getPostCount();
    }

    @GetMapping("/recent")
    public List<Post> getMostRecent() {
        return service.getMostRecent();
    }

    @GetMapping("/commented")
    public List<Post> getMostCommented() {
        return service.getMostCommented();
    }

    @GetMapping("/{id}")
    public Post getId(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public void LikePost(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            service.likePost(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PatchMapping("/{postId}/tags")
    public void addTagToPost(@RequestHeader(name = "Credentials") String credentials, @PathVariable int postId, @Valid @RequestBody TagDto tagDto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Post post = service.getById(postId);
            Tag tag = tagMapper.fromDto(tagDto);
            service.addTagToPost(user, post, tag);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{postId}/tags/{tagId}")
    public void removeTagFromPost(@RequestHeader(name = "Credentials") String credentials, @PathVariable int postId, @PathVariable int tagId) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Post post = service.getById(postId);
            Tag tag = tagService.getById(tagId);
            service.deleteTagFromPost(user, post, tag);
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Post create(@RequestHeader(name = "Credentials") String credentials, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Post post = postMapper.fromDto(postDto);
            service.create(post, user);
            return post;
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Post update(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Post post = postMapper.fromDto(id, postDto);
            service.update(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            service.delete(user,id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

package org.forum.web.forum.controllers;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.TagMapper;
import org.forum.web.forum.models.Dtos.TagDto;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.contracts.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {
    private final TagService service;
    private final TagMapper tagMapper;
    private final AuthenticationHelper authenticationHelper;

    public TagRestController(TagService service, TagMapper tagMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.tagMapper = tagMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Tag> getAll(@RequestHeader(name = "Credentials") String credentials) {
        try {
            authenticationHelper.tryGetUser(credentials);
            return service.getAll();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Tag getById(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(credentials);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public void create(@RequestHeader(name = "Credentials") String credentials, @Valid @RequestBody TagDto tagDto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Tag tag = tagMapper.fromDto(tagDto);
            service.create(tag, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(name = "Credentials") String credentials, @PathVariable int id) {
        Tag tag = service.getById(id);
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            service.delete(tag, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}

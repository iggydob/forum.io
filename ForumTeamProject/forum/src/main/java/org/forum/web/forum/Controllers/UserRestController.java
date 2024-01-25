package org.forum.web.forum.Controllers;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.UserMapper;
import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    public static final String ERROR_MESSAGE = "You are not authorized to access user information.";
    private final UserService service;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService service, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            }
            return service.getAll();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            service.create(user);
            return user;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.deleteById(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{username}")
    public void deleteByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.deleteByUsername(username, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

//    @GetMapping("/search")
//    public List<User> getAll(@RequestHeader HttpHeaders headers) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            if (!user.isAdmin()) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
//            }
//            return service.getAll();
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

//    @GetMapping
//    public List<User> getFiltered(
////            @RequestHeader HttpHeaders headers,
//            @RequestHeader(required = false) String firstName,
//            @RequestHeader(required = false) String lastName,
//            @RequestHeader(required = false) String username,
//            @RequestHeader(required = false) String email,
//            @RequestHeader(required = false) String sortBy,
//            @RequestHeader(required = false) String orderBy,
//            @RequestHeader(required = false) String sortOrder) {
//        UserFilterOptions userFilterOptions = new UserFilterOptions(
//                firstName,
//                lastName,
//                username,
//                email,
//                sortBy,
//                orderBy,
//                sortOrder);
//        return service.getFiltered(userFilterOptions);
//    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getUserId() != targetUserId) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }


}

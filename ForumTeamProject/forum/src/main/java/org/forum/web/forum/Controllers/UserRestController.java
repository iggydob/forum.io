package org.forum.web.forum.Controllers;

import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.UserMapper;
import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.PhoneNumber;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.UserFilterOptions;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    public static final String ERROR_MESSAGE = "Access denied. You are not allowed to perform this action.";
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
            if (!user.getAdminStatus()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            }
            return service.getAll();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public User create(
            @Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.dtoUserCreate(userDto);
            service.create(user);
            return user;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getById(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<User> getFiltered(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        UserFilterOptions userFilterOptions = new UserFilterOptions(
                firstName,
                lastName,
                username,
                email,
                sortBy,
                sortOrder);
        return service.getFiltered(userFilterOptions);
    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable int id,
//            @PathVariable String userType,
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userDetails = userMapper.dtoUserUpdate(userDto);
            service.update(id, userDetails, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/{status}")
    public void changeUserStatus(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id,
            @PathVariable String status,
            @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userDetails = new User();

            switch (status) {
                case "ban":
                    userDetails = userMapper.dtoUserBanStatus(userDto);
                    service.changeBanStatus(id, userDetails, user);
                    break;
                case "promote":
                    userDetails = userMapper.dtoUserAdminStatus(userDto);
                    service.changeAdminStatus(id, userDetails, user);
                    break;
            }

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/password_reset")
    public void changePassword(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id,
            @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userDetails = userMapper.dtoUserPassword(userDto);
            service.changePassword(id, userDetails, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

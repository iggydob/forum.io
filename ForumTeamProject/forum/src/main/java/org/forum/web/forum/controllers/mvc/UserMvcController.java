package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.*;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.UserMapper;
import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.UserFilterOptions;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/edit")
    public String handleUserEdit(UserDto userDto,
                                 Model model,
                                 HttpSession session) {

        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            UserDto userDetails = userMapper.userToDto(currentUser);
            model.addAttribute("currentUser", userDetails);
            return "EditUserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/edit")
    public String showUserEdit(@Valid @ModelAttribute("currentUser") UserDto userDto,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession session) {


        User requester = authenticationHelper.tryGetCurrentUser(session);

        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUser", userDto);
            return "EditUserView";
        }

        try {
            User userToUpdate = userMapper.dtoUserUpdate(userDto);
            userService.update(requester.getUserId(), userToUpdate, requester);
            return "redirect:/posts";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_error", "E-mail already exists!");
            return "EditUserView";
        }
    }
}
package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.UserMapper;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.models.Dtos.LoginDto;
import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.User;
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
    public String handleUserEdit(Model model,
                                 HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("edit", new UserDto());
        return "EditUserView";
    }

    @PostMapping("/edit")
    public String showUserEdit(@Valid @ModelAttribute("edit") UserDto userDto,
                               Model model,
                               BindingResult bindingResult,
                               HttpSession session) {
        User currentUser = authenticationHelper.tryGetCurrentUser(session);
        User requester = currentUser;
        int userId = currentUser.getUserId();

        if (bindingResult.hasErrors()) {
            return "redirect:/users/edit";
        }

        try {
            currentUser = userMapper.dtoUserUpdate(userDto);
            userService.update(userId, currentUser, requester);
            session.setAttribute("currentUser", currentUser);
            return "redirect:/users/edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}

package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.UserMapper;
import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.Dtos.UserFilterDto;
import org.forum.web.forum.models.PhoneNumber;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.UserFilterOptions;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String showUserEdit(UserDto userDto,
                               Model model,
                               HttpSession session) {

        User requester;
        try {
            requester = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            UserDto userDetails = userMapper.userToDto(currentUser);
            model.addAttribute("currentUser", userDetails);
            model.addAttribute("requester", requester);
            PhoneNumber phoneNumber = new PhoneNumber();
            if (currentUser.getAdminStatus()) {
                model.addAttribute("currentPhoneNumber", phoneNumber);
            }
            model.addAttribute("currentPhoneNumber", phoneNumber);

            return "EditUserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/edit")
    public String handleUserEdit(@Valid @ModelAttribute("currentUser") UserDto userDto,
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

    @GetMapping("/admin")
    public String showUserPanel(@ModelAttribute("filterOptions") UserFilterDto filterDto,
                                Model model,
                                HttpSession session) {
        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        UserFilterOptions filterOptions = new UserFilterOptions(
                filterDto.getFirstName(),
                filterDto.getLastName(),
                filterDto.getUsername(),
                filterDto.getEmail(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());

        List<User> users = userService.getFiltered(filterOptions);

        model.addAttribute("filterOptions", filterDto);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", userService.getByUsername(currentUser.getUsername()));

        return "AdminPanelView";
    }

    @PostMapping("{userId}/admin/ban")
    public String banUser(@PathVariable int userId,
                          Model model,
                          HttpSession session) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("currentUser", userService.getByUsername(currentUser.getUsername()));
        userService.changeBanStatusMvc(userId, true, currentUser);

        return "redirect:/users/admin";
    }

    @PostMapping("{userId}/admin/unban")
    public String unbanUser(@PathVariable int userId,
                            Model model,
                            HttpSession session) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("currentUser", userService.getByUsername(currentUser.getUsername()));
        userService.changeBanStatusMvc(userId, false, currentUser);

        return "redirect:/users/admin";
    }

    @PostMapping("{userId}/admin/promote")
    public String promoteUser(@PathVariable int userId,
                              Model model,
                              HttpSession session) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("currentUser", userService.getByUsername(currentUser.getUsername()));
        userService.changeAdminStatusMvc(userId, true, currentUser);

        return "redirect:/users/admin";
    }

    @PostMapping("{userId}/admin/demote")
    public String demoteUser(@PathVariable int userId,
                             Model model,
                             HttpSession session) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("currentUser", userService.getByUsername(currentUser.getUsername()));
        userService.changeAdminStatusMvc(userId, false, currentUser);

        return "redirect:/users/admin";
    }

}
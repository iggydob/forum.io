package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.service.contracts.PostService;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final PostService postService;

    private final UserService userService;

    @Autowired
    public HomeMvcController(
            AuthenticationHelper authenticationHelper,
            PostService postService,
            UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {
        model.addAttribute("mostCommentedPosts", postService.getMostCommented());
        model.addAttribute("mostRecentPosts", postService.getMostRecent());
        model.addAttribute("postCount",postService.getPostCount());
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            User currentUser = userService.getByUsername(username);
            model.addAttribute("currentUser", currentUser);
        }
        return "HomePageView";
    }

}

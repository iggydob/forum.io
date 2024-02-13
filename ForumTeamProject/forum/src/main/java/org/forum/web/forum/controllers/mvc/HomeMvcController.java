package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.models.Dtos.PostFilterDto;
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

import java.util.List;

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

    //show home page with all post
    @GetMapping
    public String showHomePage(@ModelAttribute("filterOptions") PostFilterDto filterDto, Model model, HttpSession session) {
        PostFilterOptions filterOptions = new PostFilterOptions(
                filterDto.getTitle(),
                filterDto.getPostAuthor(),
                filterDto.getSortPostBy(),
                filterDto.getSortOrder());
        List<Post> posts = postService.getFiltered(filterOptions);
        if (populateIsAuthenticated(session)) {
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByUsername(currentUsername));
        } else {
            model.addAttribute("mostCommentedPosts", postService.getMostCommented());
            model.addAttribute("mostRecentPosts", postService.getMostRecent());
            model.addAttribute("postCount", postService.getPostCount());
            return "HomePageNotLogged";
        }
        model.addAttribute("filterOptions", filterDto);
        model.addAttribute("posts", posts);
        model.addAttribute("postCount", postService.getPostCount());
        return "HomePageView";
    }
}

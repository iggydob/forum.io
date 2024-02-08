package org.forum.web.forum.controllers.mvc;

import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.service.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final PostService postService;

    @Autowired
    public HomeMvcController(
            AuthenticationHelper authenticationHelper,
                             PostService postService) {
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
    }

    @GetMapping
    public String showHomePage(Model model) {
//        model.addAttribute("post", postService.getFiltered(new PostFilterOptions()));
        return "HomePageView";
    }

}
